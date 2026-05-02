package dev.hytalemodding.messages;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.task.TaskRegistry;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ChaoticMessageService {

    private static final String PLAYER_PLACEHOLDER = "{player}";
    private static final ChaoticMessageAsset FALLBACK_CONFIG = new ChaoticMessageAsset();
    private final Set<World> activeWorlds = ConcurrentHashMap.newKeySet();
    private ScheduledFuture<Void> periodicTask;

    public ChaoticMessageService() {
    }

    @SuppressWarnings("unchecked")
    public void startPeriodicMessages(@Nonnull TaskRegistry taskRegistry) {
        if (periodicTask != null) {
            return;
        }
        long intervalSeconds = getConfig().getPeriodicIntervalSeconds();
        ScheduledFuture<Void> future = (ScheduledFuture<Void>) (ScheduledFuture<?>) HytaleServer.SCHEDULED_EXECUTOR
                .scheduleWithFixedDelay(this::broadcastPeriodicMessage, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
        this.periodicTask = future;
        taskRegistry.registerTask(future);
    }

    public void shutdown() {
        if (periodicTask != null) {
            periodicTask.cancel(false);
            periodicTask = null;
        }
        activeWorlds.clear();
    }

    public void trackWorld(@Nonnull World world) {
        activeWorlds.add(world);
    }

    public void untrackWorld(@Nonnull World world) {
        activeWorlds.remove(world);
    }

    public void sendJoinMessage(@Nonnull Ref<EntityStore> playerEntityRef, @Nonnull PlayerRef playerRef) {
        World world = playerEntityRef.getStore().getExternalData().getWorld();
        if (world != null) {
            trackWorld(world);
        }

        ChaoticMessageAsset currentConfig = getConfig();
        if (!currentConfig.isEnabled() || !currentConfig.isJoinMessagesEnabled()) {
            return;
        }

        SelectedMessage selectedMessage = selectJoinMessage(currentConfig);
        if (selectedMessage == null) {
            return;
        }

        send(selectedMessage, world, playerRef, playerRef.getUsername());
    }

    private void broadcastPeriodicMessage() {
        ChaoticMessageAsset currentConfig = getConfig();
        if (!currentConfig.isEnabled() || !currentConfig.isPeriodicMessagesEnabled()) {
            return;
        }

        SelectedMessage selectedMessage = selectFromCategory(currentConfig.getPeriodic());
        if (selectedMessage == null) {
            return;
        }

        for (World world : activeWorlds) {
            if (world == null || !world.isAlive()) {
                activeWorlds.remove(world);
                continue;
            }
            world.execute(() -> {
                if (world.isAlive() && world.getPlayerCount() > 0) {
                    world.sendMessage(Message.raw(format(selectedMessage.message(), "the server")));
                }
            });
        }
    }

    private SelectedMessage selectJoinMessage(@Nonnull ChaoticMessageAsset currentConfig) {
        ChaoticMessageAsset.MessageCategory joinRare = currentConfig.getJoinRare();
        if (joinRare.isEnabled() && ThreadLocalRandom.current().nextDouble() < currentConfig.getRareChance()) {
            SelectedMessage rareMessage = selectFromCategory(joinRare);
            if (rareMessage != null) {
                return rareMessage;
            }
        }

        return selectFromCategory(currentConfig.getJoin());
    }

    private SelectedMessage selectFromCategory(ChaoticMessageAsset.MessageCategory category) {
        if (category == null || !category.isEnabled()) {
            return null;
        }
        List<SelectedMessage> candidates = new ArrayList<>();
        addCategoryMessages(candidates, category);
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
    }

    private void addCategoryMessages(
            @Nonnull List<SelectedMessage> candidates,
            ChaoticMessageAsset.MessageCategory category
    ) {
        if (category == null || !category.isEnabled() || category.getMessages() == null) {
            return;
        }
        for (String message : category.getMessages()) {
            if (message != null && !message.isBlank()) {
                candidates.add(new SelectedMessage(message, resolveDelivery(category)));
            }
        }
    }

    private Delivery resolveDelivery(@Nonnull ChaoticMessageAsset.MessageCategory category) {
        return "Player".equalsIgnoreCase(category.getDelivery()) ? Delivery.PLAYER : Delivery.SERVER;
    }

    private void send(
            @Nonnull SelectedMessage selectedMessage,
            World world,
            @Nonnull PlayerRef playerRef,
            @Nonnull String username
    ) {
        Message message = Message.raw(format(selectedMessage.message(), username));
        if (selectedMessage.delivery() == Delivery.PLAYER || world == null) {
            playerRef.sendMessage(message);
            return;
        }
        if (!broadcastToActiveWorlds(message)) {
            world.sendMessage(message);
        }
    }

    private boolean broadcastToActiveWorlds(@Nonnull Message message) {
        boolean sent = false;
        for (World activeWorld : activeWorlds) {
            if (activeWorld == null || !activeWorld.isAlive()) {
                activeWorlds.remove(activeWorld);
                continue;
            }
            if (activeWorld.getPlayerCount() > 0) {
                activeWorld.sendMessage(message);
                sent = true;
            }
        }
        return sent;
    }

    private String format(@Nonnull String message, @Nonnull String username) {
        return message.replace(PLAYER_PLACEHOLDER, username);
    }

    private ChaoticMessageAsset getConfig() {
        ChaoticMessageAsset asset = ChaoticMessageAsset.getDefaultAsset();
        return asset == null ? FALLBACK_CONFIG : asset;
    }

    private enum Delivery {
        SERVER,
        PLAYER
    }

    private record SelectedMessage(String message, Delivery delivery) {
    }
}
