package me.adrianed.chatregulator.api.event;

import me.adrianed.chatregulator.api.event.result.EventResult;

public interface ResultedEvent<R extends EventResult> extends RegulatorEvent {
    R result();
}
