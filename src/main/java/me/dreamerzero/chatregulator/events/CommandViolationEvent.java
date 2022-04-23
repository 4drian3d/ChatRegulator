package me.dreamerzero.chatregulator.events;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Event fired when recognizing an infraction in a command executed by a player.
 */
public final class CommandViolationEvent extends ViolationEvent {
    private final String command;

    /**
     * Constructor of a CommandViolationEvent
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param command the executed command in which the violation was found
     * @param result the result of the detection
     */
    public CommandViolationEvent(
        @NotNull InfractionPlayer infractionPlayer,
        @NotNull InfractionType type,
        @NotNull me.dreamerzero.chatregulator.result.Result result,
        @NotNull String command){

            super(Objects.requireNonNull(infractionPlayer), type, Objects.requireNonNull(result));
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
