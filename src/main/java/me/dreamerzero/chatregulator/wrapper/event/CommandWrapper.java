package me.dreamerzero.chatregulator.wrapper.event;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.enums.SourceType;

public class CommandWrapper extends EventWrapper<CommandExecuteEvent> {

    public CommandWrapper(CommandExecuteEvent event, Continuation continuation) {
        super(event, continuation);
    }

    @Override
    public void cancel() {
        this.event.setResult(CommandResult.denied());
    }

    @Override
    public void setString(String string) {
        this.event.setResult(CommandResult.command(string));
    }

    @Override
    public SourceType source() {
        return SourceType.COMMAND;
    }

    @Override
    public boolean shouldReplace() {
        if(event.getCommandSource() instanceof Player player) {
            return player.getIdentifiedKey() == null
                || player.getProtocolVersion().getProtocol() < 760;
        }
        return true;
    }

}
