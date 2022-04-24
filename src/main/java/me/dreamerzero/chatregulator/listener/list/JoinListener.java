package me.dreamerzero.chatregulator.listener.list;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.InfractionPlayer;

/**
 * Join Listener for creation of InfractionPlayers
 */
@Internal
public final class JoinListener {
    /**
     * Listener for Player Join
     * Used for the creation of new {@link InfractionPlayer}
     * @param event the login event
     */
    @Subscribe(order = PostOrder.LAST)
    public EventTask onPlayerJoin(final PostLoginEvent event){
        return EventTask.async(() -> {
            final Player player = event.getPlayer();
            final InfractionPlayer p = InfractionPlayer.get(player);
            p.isOnline(true);
        });
    }
}
