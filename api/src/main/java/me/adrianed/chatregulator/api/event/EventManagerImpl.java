package me.adrianed.chatregulator.api.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import me.adrianed.chatregulator.api.logger.RegulatorLogger;

class EventManagerImpl implements EventManager {
    private final Map<Class<? extends RegulatorEvent>, Collection<EventHandler<RegulatorEvent>>> listeners = new HashMap<>();
    private final ExecutorService executor;

    EventManagerImpl(RegulatorLogger logger) {
        this.executor = Executors.newCachedThreadPool(
            new ThreadFactoryBuilder()
                .setNameFormat("ChatRegulator-Thread-%d")
                .setUncaughtExceptionHandler(
                    (t, ex) -> logger.error("An error ocurred on event processing in thread %s", t, ex))
                .build()
        );
    }
    @Override
    @SuppressWarnings("unchecked")
    public <E extends RegulatorEvent> void register(Class<E> clazz, EventHandler<E> handler) {
        var eventListeners = listeners.get(clazz);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            listeners.put(clazz, eventListeners);
        }
        eventListeners.add((EventHandler<RegulatorEvent>)handler);
        
    }

    @Override
    public <E extends RegulatorEvent> void unregister(Class<E> clazz, EventHandler<E> handler) {
        var eventListeners = listeners.get(clazz);
        if (eventListeners == null) return;

        eventListeners.remove(handler);
        
    }

    @Override
    public <E extends RegulatorEvent> CompletableFuture<E> fire(E event) {
        var list = listeners.get(event.getClass());
        if(list == null) return CompletableFuture.completedFuture(event);

        return CompletableFuture.supplyAsync(() -> {
            list.forEach(handler -> handler.handle(event));
            return event;
        }, executor);
    }

    @Override
    public <E extends RegulatorEvent> void fireAndForget(E event) {
        var list = listeners.get(event.getClass());
        if(list == null) return;

        executor.execute(() -> 
            list.forEach(handler -> handler.handle(event))
        );
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends RegulatorEvent> Collection<EventHandler<E>> registeredListeners(Class<E> clazz) {
        return listeners.get(clazz).stream().map(event -> (EventHandler<E>)event).toList();
    }
    
}
