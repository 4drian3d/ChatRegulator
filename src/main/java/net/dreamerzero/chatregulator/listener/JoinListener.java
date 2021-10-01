package net.dreamerzero.chatregulator.listener;

import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;

public class JoinListener {
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        InfractionPlayer infractionPlayer = new InfractionPlayer(player);
        var infractionPlayers = Regulator.getInfractionPlayers();
        if(!infractionPlayers.containsKey(playerUUID)){
            infractionPlayers.put(playerUUID, infractionPlayer);
        }
    }
}
