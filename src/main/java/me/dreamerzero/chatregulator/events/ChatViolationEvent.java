package me.dreamerzero.chatregulator.events;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Event fired when recognizing an infraction in the chat of a player
 */
public class ChatViolationEvent extends ViolationEvent {
    private final String message;

    /**
     * Constructor of a ChatViolationEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param detectionResult the detection result
     * @param message the chat message in which the violation was found
     */
    public ChatViolationEvent(
        @NotNull InfractionPlayer infractionPlayer,
        @NotNull InfractionType type,
        @NotNull me.dreamerzero.chatregulator.result.Result detectionResult,
        @NotNull String message) {

            super(infractionPlayer, type, detectionResult);
            this.message = message;
    }

    /**
     * Get the message from which the infraction was detected
     * @return the infraction message
     * @since 1.1.0
     */
    public @NotNull String getMessage(){
        return this.message;
    }
}
