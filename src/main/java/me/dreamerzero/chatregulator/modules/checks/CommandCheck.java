package me.dreamerzero.chatregulator.modules.checks;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.CommandUtils;

/**
 * Check for verification of executed commands
 */
public class CommandCheck extends AbstractCheck {
    private final Set<String> blockedCommands;

    public CommandCheck(){
        this.blockedCommands = Configuration.getBlacklist().getBlockedCommands();
    }

    @Override
    public CompletableFuture<? extends Result> check(@NotNull String message) {
        for (String blockedCommand : blockedCommands){
            if(CommandUtils.isStartingString(message, blockedCommand)){
                return CompletableFuture.completedFuture(new Result(message, true));
            }
        }
        return CompletableFuture.completedFuture(new Result(message, false));
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.BCOMMAND;
    }

}
