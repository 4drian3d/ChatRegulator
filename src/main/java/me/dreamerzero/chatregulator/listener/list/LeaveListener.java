package me.dreamerzero.chatregulator.listener.list;

import java.time.Instant;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.InfractionPlayer;

@Internal
public final class LeaveListener {
    /**
     * In case the player leaves the server,
     * his online status is marked as false.
     * @param event the event
     */
    @Subscribe
    public EventTask onLeave(DisconnectEvent event){
        return EventTask.async(()->{
            InfractionPlayer player = InfractionPlayer.get(event.getPlayer());
            player.isOnline(false);
            player.setLastSeen(Instant.now());
        });
    }
}
