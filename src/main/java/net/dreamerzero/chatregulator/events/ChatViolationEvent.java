package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Event fired when recognizing an infraction in the chat of a player
 */
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

    /**
     * Get the InfractorPlayer that has committed the infraction
     * To get the original player, check {@link InfractionPlayer#getPlayer()}
     * @return the infractor
     * @since 1.1.0
     */
    public InfractionPlayer getInfractionPlayer(){
        return infractionPlayer;
    }

    /**
     * Get the type of infraction committed
     * @return the infraction committed
     * @since 1.1.0
     */
    public InfractionType getType(){
        return type;
    }

    /**
     * Get the message from which the infraction was detected
     * @return the infraction message
     * @since 1.1.0
     */
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
