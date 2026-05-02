package dev.hytalemodding;

import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.server.core.asset.HytaleAssetStore;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.universe.world.events.RemoveWorldEvent;
import dev.hytalemodding.assets.AHytaleModAssetPackCoordinator;
import dev.hytalemodding.commands.ExampleCommand;
import dev.hytalemodding.events.ChaoticMessageEvent;
import dev.hytalemodding.events.ExampleEvent;
import dev.hytalemodding.messages.ChaoticMessageAsset;
import dev.hytalemodding.messages.ChaoticMessageService;

import javax.annotation.Nonnull;

public class AHytaleMod extends JavaPlugin {

    private ChaoticMessageService chaoticMessageService;
    private AHytaleModAssetPackCoordinator assetPackCoordinator;
    private boolean chaoticMessageAssetsRegistered;

    public AHytaleMod(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.assetPackCoordinator = new AHytaleModAssetPackCoordinator(this);
        registerChaoticMessageAssets();
        this.chaoticMessageService = new ChaoticMessageService();

        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
        ChaoticMessageEvent chaoticMessageEvent = new ChaoticMessageEvent(this.chaoticMessageService);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, chaoticMessageEvent::onPlayerReady);
        this.getEventRegistry().registerGlobal(AddWorldEvent.class, event -> this.chaoticMessageService.trackWorld(event.getWorld()));
        this.getEventRegistry().registerGlobal(RemoveWorldEvent.class, event -> this.chaoticMessageService.untrackWorld(event.getWorld()));
        this.chaoticMessageService.startPeriodicMessages(this.getTaskRegistry());
    }

    @Override
    protected void start() {
        if (this.assetPackCoordinator != null) {
            this.assetPackCoordinator.ensureAssetEditorPackVisible();
        }
    }

    private void registerChaoticMessageAssets() {
        if (chaoticMessageAssetsRegistered) {
            return;
        }
        getAssetRegistry().register(
                HytaleAssetStore.builder(ChaoticMessageAsset.class, new DefaultAssetMap<>())
                        .setPath("ChaoticMessages")
                        .setCodec(ChaoticMessageAsset.CODEC)
                        .setKeyFunction(ChaoticMessageAsset::getId)
                        .build()
        );
        chaoticMessageAssetsRegistered = true;
    }

    @Override
    protected void shutdown() {
        if (this.chaoticMessageService != null) {
            this.chaoticMessageService.shutdown();
            this.chaoticMessageService = null;
        }
        this.assetPackCoordinator = null;
    }
}
