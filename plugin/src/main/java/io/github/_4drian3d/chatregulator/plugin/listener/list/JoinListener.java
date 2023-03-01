package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.jetbrains.annotations.ApiStatus.Internal;

/**
 * Join Listener for creation of InfractionPlayers
 */
@Internal
public final class JoinListener {
    /**
     * Listener for Player Join
     * Used for the creation of new {@link InfractionPlayerImpl}
     * @param event the login event
     */
    @Subscribe(order = PostOrder.LAST)
    public EventTask onPlayerJoin(final PostLoginEvent event){
        return EventTask.async(() -> InfractionPlayerImpl.get(event.getPlayer()).isOnline(true));
    }
}
