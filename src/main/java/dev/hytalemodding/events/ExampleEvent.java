package dev.hytalemodding.events;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

public class ExampleEvent {

    public static void onPlayerReady(PlayerReadyEvent event) {
        PlayerRef playerRef = event.getPlayer();
        playerRef.sendMessage(Message.raw("Welcome " + playerRef.getDisplayName()));
        System.out.println("Someone joined the server!");
    }

}
