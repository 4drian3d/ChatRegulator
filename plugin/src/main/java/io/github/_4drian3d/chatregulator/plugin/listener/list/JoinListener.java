package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;

public final class JoinListener implements RegulatorExecutor<PostLoginEvent> {
    @Inject
    private PlayerManagerImpl playerManager;

    @Override
    public EventTask executeAsync(PostLoginEvent event) {
        return EventTask.async(() -> this.playerManager.getPlayer(event.getPlayer()).isOnline(true));
    }

    @Override
    public Class<PostLoginEvent> eventClass() {
        return PostLoginEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.LAST;
    }
}
