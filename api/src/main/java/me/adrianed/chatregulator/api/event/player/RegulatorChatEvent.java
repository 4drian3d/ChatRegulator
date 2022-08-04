package me.adrianed.chatregulator.api.event.player;

import me.adrianed.chatregulator.api.RegulatorUser;
import me.adrianed.chatregulator.api.event.ResultedEvent;
import me.adrianed.chatregulator.api.event.result.StringResult;

public class RegulatorChatEvent implements ResultedEvent<StringResult> {
    private final RegulatorUser user;
    private StringResult result;
    private boolean handled = false;

    public RegulatorChatEvent(RegulatorUser user) {
        this.user = user;
    }

    public RegulatorUser user() {
        return this.user;
    }

    public void result(StringResult result) {
        this.result = result;
        handled = true;
    }

    @Override
    public StringResult result() {
        return this.result;
    }

    @Override
    public boolean handled() {
        return this.handled;
    }
}
