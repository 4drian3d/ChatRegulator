package io.github._4drian3d.chatregulator.plugin.wrapper;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.ResultedEvent;

import io.github._4drian3d.chatregulator.api.enums.SourceType;

public abstract class EventWrapper<E extends ResultedEvent<?>> {
    protected final E event;
    protected final Continuation continuation;
    protected EventWrapper(E event, Continuation continuation) {
        this.event = event;
        this.continuation = continuation;
    }
    public abstract void cancel();

    public abstract void setString(String string);

    public void resume() {
        continuation.resume();
    }

    public abstract SourceType source();
}
