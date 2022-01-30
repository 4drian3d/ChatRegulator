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
     * ans returns a CompletableFuture with the correspondient
     * @param message the message to check
     */
    public abstract CompletableFuture<Result> check(@NotNull String message);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    public abstract @NotNull InfractionType type();
}
