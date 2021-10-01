package net.dreamerzero.chatregulator.listener;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.Regulator;

public class LeaveListener {
    @Subscribe
    public void onLeave(DisconnectEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        var infractionPlayers = Regulator.getInfractionPlayers();
        if(infractionPlayers.containsKey(playerUUID)){
            infractionPlayers.remove(playerUUID);
        }
    }
}
