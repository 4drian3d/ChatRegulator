package io.github._4drian3d.chatregulator.api.event;

import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;

import static java.util.Objects.requireNonNull;

/**
 * Event fired when recognizing an infraction in a command executed by a player.
 */
public final class CommandInfractionEvent extends InfractionEvent {
    private final String command;

    /**
     * Constructor of a CommandInfractionEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param command the executed command in which the violation was found
     * @param result the result of the detection
     */
    public CommandInfractionEvent(
        @NotNull InfractionPlayer infractionPlayer,
        @NotNull InfractionType type,
        @NotNull CheckResult result,
        @NotNull String command
    ) {
        super(requireNonNull(infractionPlayer), type, requireNonNull(result));
        this.command = command;
    }

    /**
     * Get the command from which the infraction was detected
     * @return the infraction command
     * @since 1.1.0
     */
    public @NotNull String getCommand(){
        return this.command;
    }
}
