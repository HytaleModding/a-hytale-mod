package dev.hytalemodding.events;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class ExampleEvent {

    public static void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        PlayerRef playerRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;
        playerRef.sendMessage(Message.raw("Welcome " + playerRef.getUsername()));
        System.out.println("Someone joined the server!");
    }

}
