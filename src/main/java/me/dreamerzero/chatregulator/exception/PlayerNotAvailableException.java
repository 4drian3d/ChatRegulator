package me.dreamerzero.chatregulator.exception;

import java.util.Objects;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

public class PlayerNotAvailableException extends RuntimeException{
    public PlayerNotAvailableException(){
        super("This player is not available");
    }

    public PlayerNotAvailableException(@NotNull String reason){
        super(Objects.requireNonNull(reason));
    }

    public PlayerNotAvailableException(@NotNull String reason, @NotNull Player player){
        super(reason.replace("<player>", Objects.requireNonNull(player).getUsername()));
    }

    public PlayerNotAvailableException(@NotNull UUID uuid){
        super("The player with UUID "+Objects.requireNonNull(uuid).toString()+" are not available");
    }
}
