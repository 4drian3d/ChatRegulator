package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class CommandViolationEvent implements ResultedEvent<GenericResult> {
    private final InfractionPlayer infractionPlayer;
    private final InfractionType type;
    private final String command;
    private GenericResult result = GenericResult.allowed();

    public CommandViolationEvent(InfractionPlayer infractionPlayer, InfractionType type, String command){
        this.infractionPlayer = infractionPlayer;
        this.type = type;
        this.command = command;
    }

    public InfractionPlayer getInfractionPlayer(){
        return infractionPlayer;
    }

    public InfractionType getType(){
        return type;
    }

    public String getCommand(){
        return command;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
