package net.dreamerzero.chatregulator.listener.list;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import net.dreamerzero.chatregulator.InfractionPlayer;

public class LeaveListener {
    /**
     * In case the player leaves the server,
     * his online status is marked as false.
     * @param event the event
     */
    @Subscribe(async = true)
    public void onLeave(DisconnectEvent event){
        InfractionPlayer infractionPlayer = InfractionPlayer.get(event.getPlayer());
        infractionPlayer.isOnline(false);
        infractionPlayer.setLastSeen();
    }
}
