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

    // Fixes chat messages not replaceable on Minecraft 1.19.1, muchas gracias Mojang
    // https://github.com/PaperMC/Velocity/pull/772/commits/eb32a765206383a828445ba11789c9a88ca2b585#diff-841534de988d03173f1983984bc82b9fe24e2bd3296db704a3bd6412b32674ea
    public abstract boolean shouldReplace();
}
