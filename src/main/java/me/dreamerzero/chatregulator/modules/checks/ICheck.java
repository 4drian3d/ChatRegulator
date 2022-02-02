package me.dreamerzero.chatregulator.modules.checks;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;

/**
 * Base class of the checks used in the plugin
 */
public interface ICheck {
    /**
     * Check if the delivered string contains any infraction
     * ans returns a CompletableFuture with the correspondient Result
     * @param message the message to check
     * @see Result
     * @since 3.0.0
     * @apiNote To see what check has returned, perform a result instanceof ReplaceableResult
     * @return a CompletableFuture with the result of the check
     */
    public abstract CompletableFuture<Result> check(@NotNull final String message);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    public abstract @NotNull InfractionType type();
}
