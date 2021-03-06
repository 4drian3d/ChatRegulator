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
     * To see what check has returned, perform a check by instanceof
     * @param string the string to check
     * @see Result
     * @since 3.0.0
     * @return a CompletableFuture with the result of the check
     */
    @NotNull CompletableFuture<Result> check(final @NotNull String string);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    @NotNull InfractionType type();
}
