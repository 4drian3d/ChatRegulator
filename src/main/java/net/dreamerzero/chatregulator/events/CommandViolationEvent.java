package net.dreamerzero.chatregulator.events;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Event fired when recognizing an infraction in a command executed by a player.
 */
public class CommandViolationEvent extends ViolationEvent {
    private final String command;

    /**
     * Constructor of a CommandViolationEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param command the executed command in which the violation was found
     */
    public CommandViolationEvent(InfractionPlayer infractionPlayer, InfractionType type, String command){
        super(infractionPlayer, type);
        this.command = command;
    }

    /**
     * Get the command from which the infraction was detected
     * @return the infraction command
     * @since 1.1.0
     */
    public String getCommand(){
        return this.command;
    }
}
