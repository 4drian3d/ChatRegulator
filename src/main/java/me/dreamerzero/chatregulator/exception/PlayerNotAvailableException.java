package me.dreamerzero.chatregulator.exception;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

/**Exception for unavailability of a player*/
public final class PlayerNotAvailableException extends RuntimeException{
    /**
     * Creates a new exception without message
     */
    public PlayerNotAvailableException(){
        super("This player is not available");
    }

    /**
     * Creates a new exception with the specified message
     * @param reason the reason
     */
    public PlayerNotAvailableException(@NotNull String reason){
        super(Objects.requireNonNull(reason));
    }

    /**
     * Creates a new exception with an {@link UUID}
     * @param uuid the uuid of the unavailable player
     */
    public PlayerNotAvailableException(@NotNull UUID uuid){
        super("The player with UUID "+Objects.requireNonNull(uuid).toString()+" are not available");
    }
}
