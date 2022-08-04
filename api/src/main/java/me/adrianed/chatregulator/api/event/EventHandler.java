package me.adrianed.chatregulator.api.event;

@FunctionalInterface
public interface EventHandler<E extends RegulatorEvent> {
    void handle(E event);
}
