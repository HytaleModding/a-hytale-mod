package dev.hytalemodding.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.messages.ChaoticMessageService;

import javax.annotation.Nonnull;

public class ChaoticMessageEvent {

    private final ChaoticMessageService chaoticMessageService;

    public ChaoticMessageEvent(@Nonnull ChaoticMessageService chaoticMessageService) {
        this.chaoticMessageService = chaoticMessageService;
    }

    public void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;
        if (playerRef == null) {
            return;
        }
        chaoticMessageService.sendJoinMessage(ref, playerRef);
        System.out.println(playerRef.getUsername() + " joined the server and triggered chaotic welcome messaging!");
    }

}
