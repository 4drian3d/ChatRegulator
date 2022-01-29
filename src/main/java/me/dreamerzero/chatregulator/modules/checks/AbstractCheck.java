package me.dreamerzero.chatregulator.modules.checks;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;

/**
 * Base class of the checks used in the plugin
 */
public abstract class AbstractCheck {
    protected boolean detected;
    protected String string;

    /**
     * Check if the delivered string contains any infraction
     * @param message the message to check
     */
    //public abstract void check(@NotNull String message);

    /**
     * Check if the delivered string contains any infraction
     * @param message the message to check
     */
    public abstract CompletableFuture<? extends Result> check(@NotNull String message);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    public abstract @NotNull InfractionType type();

    /**
     * Check the detection result
     * @return the result
     */
    /*public boolean isInfraction(){
        return this.detected;
    }*/

    /**
     * Get the word involved in the detection
     * @return the infraction word
     */
    /*public @Nullable String getInfractionWord(){
        return this.string;
    }*/
}
