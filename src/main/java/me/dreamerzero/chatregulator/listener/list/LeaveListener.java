package me.dreamerzero.chatregulator.listener.list;

import java.time.Instant;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import me.dreamerzero.chatregulator.InfractionPlayer;

public class LeaveListener {
    /**
     * In case the player leaves the server,
     * his online status is marked as false.
     * @param event the event
     */
    @Subscribe(async = true)
    public void onLeave(DisconnectEvent event){
        InfractionPlayer.get(event.getPlayer())
            .isOnline(false)
            .setLastSeen(Instant.now());
    }
}
