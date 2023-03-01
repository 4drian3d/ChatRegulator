package io.github._4drian3d.chatregulator.api.event;

import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;

/**
 * Event fired when recognizing an infraction in the chat of a player
 */
public final class ChatViolationEvent extends ViolationEvent {
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
        @NotNull io.github._4drian3d.chatregulator.api.result.Result detectionResult,
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
