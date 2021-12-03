package net.dreamerzero.chatregulator.exception;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

public class PlayerNotAvailableException extends Exception{
    public PlayerNotAvailableException(){
        super("This player is not available");
    }

    public PlayerNotAvailableException(String reason){
        super(reason);
    }

    public PlayerNotAvailableException(String reason, Player player){
        super(reason.replace("<player>", player.getUsername()));
    }

    public PlayerNotAvailableException(UUID uuid){
        super("The player with UUID "+uuid.toString()+" are not available");
    }
}
