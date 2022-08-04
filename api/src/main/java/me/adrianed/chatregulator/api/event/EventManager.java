package me.adrianed.chatregulator.api.event;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface EventManager {
    <E extends RegulatorEvent> void register(Class<E> clazz, EventHandler<E> handler);

    <E extends RegulatorEvent> void unregister(Class<E> clazz, EventHandler<E> handler);

    <E extends RegulatorEvent> CompletableFuture<E> fire(E event);

    <E extends RegulatorEvent> void fireAndForget(E event);

    <E extends RegulatorEvent> Collection<EventHandler<E>> registeredListeners(Class<E> clazz);
}
