package io.github._4drian3d.chatregulator.api.event;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

/**
 * Basis for infringement events
 */
public sealed abstract class InfractionEvent permits ChatInfractionEvent, CommandInfractionEvent {
    /**
     * InfractionPlayer involved in detection
     */
    private final InfractionPlayer infractionPlayer;
    /**
     * Type of detection
     */
    private final InfractionType type;
    private final CheckResult detectionResult;

    /**
     * InfractionEvent Constructor
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param detectionResult the result of the detection
     */
    protected InfractionEvent(
            final @NotNull InfractionPlayer infractionPlayer,
            final @NotNull InfractionType type,
            final @NotNull CheckResult detectionResult
    ) {
        this.infractionPlayer = infractionPlayer;
        this.type = type;
        this.detectionResult = detectionResult;
    }

    /**
     * Get the InfractionPlayer that has committed the infraction
     * @return the player
     * @since 1.1.0
     */
    public @NotNull InfractionPlayer getInfractor(){
        return this.infractionPlayer;
    }

    /**
     * Get the type of infraction committed
     * @return the infraction committed
     * @since 1.1.0
     */
    public @NotNull InfractionType getType(){
        return this.type;
    }

    /**
     * Obtain the detection performed
     * With this object, you can get the pattern,
     * the detected string and more.
     * @return the detection performed
     */
    public @NotNull CheckResult getDetectionResult(){
        return this.detectionResult;
    }
}
