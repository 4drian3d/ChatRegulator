package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.google.inject.Inject;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class JoinListener implements AwaitingEventExecutor<PostLoginEvent> {
    @Inject
    private PlayerManagerImpl playerManager;

    //PostOrder.LAST
    @Override
    public EventTask executeAsync(PostLoginEvent event) {
        return EventTask.async(() -> playerManager.getPlayer(event.getPlayer()).isOnline(true));
    }
}
