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
public class CommandCheck implements ICheck {
    @Override
    public CompletableFuture<Result> check(@NotNull String message) {
        final Set<String> blockedCommands = Configuration.getBlacklist().getBlockedCommands();
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
