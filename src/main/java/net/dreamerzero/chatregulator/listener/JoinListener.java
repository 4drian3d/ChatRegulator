package net.dreamerzero.chatregulator.listener;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.utils.InfractionPlayer;

public class JoinListener {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    public JoinListener(Map<UUID, InfractionPlayer> infractionPlayers){
        this.infractionPlayers = infractionPlayers;
    }
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        InfractionPlayer infractionPlayer = new InfractionPlayer(player);
        if(!infractionPlayers.containsKey(playerUUID)){
            infractionPlayers.put(playerUUID, infractionPlayer);
        }
    }
}
