package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class ChatViolationEvent implements ResultedEvent<GenericResult> {
    private final InfractionPlayer infractionPlayer;
    private final InfractionType type;
    private final String message;
    private GenericResult result = GenericResult.allowed();

    public ChatViolationEvent(InfractionPlayer infractionPlayer, InfractionType type, String message){
        this.infractionPlayer = infractionPlayer;
        this.type = type;
        this.message = message;
    }

    public InfractionPlayer getInfractionPlayer(){
        return infractionPlayer;
    }

    public InfractionType getType(){
        return type;
    }

    public String getMessage(){
        return message;
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
