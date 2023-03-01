package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import io.github._4drian3d.chatregulator.plugin.ChatRegulator;

public final class JoinListener {
    @Inject
    private ChatRegulator plugin;
    @Subscribe(order = PostOrder.LAST)
    public EventTask onPlayerJoin(final PostLoginEvent event){
        return EventTask.async(() -> plugin.getPlayerManager().getPlayer(event.getPlayer()).isOnline(true));
    }
}
