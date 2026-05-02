package dev.hytalemodding;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent;
import com.hypixel.hytale.server.core.universe.world.events.RemoveWorldEvent;
import com.hypixel.hytale.server.core.util.Config;
import dev.hytalemodding.commands.ExampleCommand;
import dev.hytalemodding.events.ChaoticMessageEvent;
import dev.hytalemodding.messages.ChaoticMessageConfig;
import dev.hytalemodding.messages.ChaoticMessageService;

import javax.annotation.Nonnull;

public class AHytaleMod extends JavaPlugin {

    private final Config<ChaoticMessageConfig> chaoticMessageConfig;
    private ChaoticMessageService chaoticMessageService;

    public AHytaleMod(@Nonnull JavaPluginInit init) {
        super(init);
        this.chaoticMessageConfig = this.withConfig("chaotic-messages", ChaoticMessageConfig.CODEC);
    }

    @Override
    protected void setup() {
        this.chaoticMessageConfig.save();
        this.chaoticMessageService = new ChaoticMessageService(this.chaoticMessageConfig);

        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        ChaoticMessageEvent chaoticMessageEvent = new ChaoticMessageEvent(this.chaoticMessageService);
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, chaoticMessageEvent::onPlayerReady);
        this.getEventRegistry().registerGlobal(AddWorldEvent.class, event -> this.chaoticMessageService.trackWorld(event.getWorld()));
        this.getEventRegistry().registerGlobal(RemoveWorldEvent.class, event -> this.chaoticMessageService.untrackWorld(event.getWorld()));
        this.chaoticMessageService.startPeriodicMessages(this.getTaskRegistry());
    }

    @Override
    protected void shutdown() {
        if (this.chaoticMessageService != null) {
            this.chaoticMessageService.shutdown();
            this.chaoticMessageService = null;
        }
    }
}
