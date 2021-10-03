package net.dreamerzero.chatregulator.listener.list;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.utils.InfractionPlayer;

public class LeaveListener {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    public LeaveListener(Map<UUID, InfractionPlayer> infractionPlayers){
        this.infractionPlayers = infractionPlayers;
    }
    @Subscribe
    public void onLeave(DisconnectEvent event){
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        InfractionPlayer infractionPlayer = infractionPlayers.get(playerUUID);
        infractionPlayer.isOnline(false);
    }
}
