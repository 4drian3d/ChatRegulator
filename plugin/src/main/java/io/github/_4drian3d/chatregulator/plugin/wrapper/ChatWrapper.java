package io.github._4drian3d.chatregulator.plugin.wrapper;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;

import io.github._4drian3d.chatregulator.api.enums.SourceType;

public class ChatWrapper extends EventWrapper<PlayerChatEvent> {

    public ChatWrapper(PlayerChatEvent event, Continuation continuation) {
        super(event,continuation);
    }

    @Override
    public void cancel() {
        this.event.setResult(ChatResult.denied());
    }

    @Override
    public void setString(String string) {
        this.event.setResult(ChatResult.message(string));
    }

    @Override
    public SourceType source() {
        return SourceType.CHAT;
    }
}
