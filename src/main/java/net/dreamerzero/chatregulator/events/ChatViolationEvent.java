package net.dreamerzero.chatregulator.events;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Event fired when recognizing an infraction in the chat of a player
 */
public class ChatViolationEvent extends ViolationEvent {
    private final String message;

    /**
     * Constructor of a ChatViolationEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param message the chat message in which the violation was found
     */
    public ChatViolationEvent(InfractionPlayer infractionPlayer, InfractionType type, AbstractCheck detection, String message) {
        super(infractionPlayer, type, detection);
        this.message = message;
    }

    /**
     * Get the message from which the infraction was detected
     * @return the infraction message
     * @since 1.1.0
     */
    public String getMessage(){
        return this.message;
    }
}
