package me.dreamerzero.chatregulator.listener.list;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Continuation;
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
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event, Continuation continuation){
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        InfractionPlayer p = infractionPlayers.get(playerUUID);
        if(p != null) {
            p.isOnline(true);
            continuation.resume();
            return;
        }

        p = new InfractionPlayer(player);
        infractionPlayers.put(playerUUID, p);
        continuation.resume();
    }
}
