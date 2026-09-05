package io.github._4drian3d.chatregulator.plugin.listener;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventManager;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;

public interface RegulatorExecutor<E> extends AwaitingEventExecutor<E> {
    default void register(final ChatRegulator plugin, final EventManager eventManager) {
        eventManager.register(plugin, eventClass(), order(), this);
    }

    Class<E> eventClass();

    short order();
}
