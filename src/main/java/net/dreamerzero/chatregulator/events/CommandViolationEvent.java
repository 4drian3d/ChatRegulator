package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Event fired when recognizing an infraction in a command executed by a player.
 */
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
     * Get the command from which the infraction was detected
     * @return the infraction command
     * @since 1.1.0
     */
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
