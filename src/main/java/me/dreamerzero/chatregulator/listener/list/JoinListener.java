package me.dreamerzero.chatregulator.listener.list;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;

/**
 * Join Listener for creation of InfractionPlayers
 */
public class JoinListener {
    private Map<UUID, InfractionPlayer> infractionPlayers;
    public JoinListener(Map<UUID, InfractionPlayer> infractionPlayers){
        this.infractionPlayers = infractionPlayers;
    }

    /**
     * Listener for Player Join
     * Used for the creation of new {@link InfractionPlayer}
     * @param event the login event
     */
    @Subscribe(async = true)
    public void onPlayerJoin(PostLoginEvent event){
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        if(infractionPlayers.containsKey(playerUUID)) {
            infractionPlayers.get(playerUUID).isOnline(true);
            return;
        }

        InfractionPlayer infractionPlayer = new InfractionPlayer(player);
        infractionPlayers.put(playerUUID, infractionPlayer);
    }
}
