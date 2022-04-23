package me.dreamerzero.chatregulator.wrapper.event;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.ResultedEvent;

import me.dreamerzero.chatregulator.enums.SourceType;

public abstract class EventWrapper<E extends ResultedEvent<?>> {
    protected final E event;
    protected final Continuation continuation;
    protected EventWrapper(E event, Continuation continuation) {
        this.event = event;
        this.continuation = continuation;
    }
    public abstract void cancel();

    public abstract void setString(String string);

    public boolean isAllowed() {
        return event.getResult().isAllowed();
    }

    public void resume() {
        continuation.resume();
    }

    public abstract SourceType source();
}
