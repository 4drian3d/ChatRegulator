package io.github._4drian3d.chatregulator.plugin.listener;

import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.PostOrder;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;

public interface RegulatorExecutor<E> extends AwaitingEventExecutor<E> {
    default void register(ChatRegulator plugin, EventManager eventManager) {
        eventManager.register(plugin, eventClass(), postOrder(), this);
    }

    Class<E> eventClass();

    PostOrder postOrder();
}
