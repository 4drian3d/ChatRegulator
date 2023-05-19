package io.github._4drian3d.chatregulator.api.event;

import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;

/**
 * Event fired when recognizing an infraction in the chat of a player
 */
public final class ChatInfractionEvent extends InfractionEvent {
    private final String message;

    /**
     * Constructor of a ChatInfractionEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param detectionResult the detection result
     * @param message the chat message in which the violation was found
     */
    public ChatInfractionEvent(
        final @NotNull InfractionPlayer infractionPlayer,
        final @NotNull InfractionType type,
        final @NotNull CheckResult detectionResult,
        final @NotNull String message
    ) {
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
