package me.dreamerzero.chatregulator.modules.checks;

import java.util.Arrays;
import java.util.Collection;
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
    private final Collection<String> blockedCommands;

    private CommandCheck(){
        this(Configuration.getBlacklist().getBlockedCommands());
    }

    private CommandCheck(Collection<String> blocledCommands){
        this.blockedCommands = blocledCommands;
    }
    @Override
    public CompletableFuture<Result> check(@NotNull String string) {
        for (String blockedCommand : blockedCommands){
            if(CommandUtils.isStartingString(string, blockedCommand)){
                return CompletableFuture.completedFuture(new Result(string, true));
            }
        }
        return CompletableFuture.completedFuture(new Result(string, false));
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.BCOMMAND;
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new CommandCheck().check(string);
    }

    public static CommandCheck.Builder builder(){
        return new CommandCheck.Builder();
    }

    public static class Builder {
        private Collection<String> blockedCommands;

        private Builder(){}

        public Builder blockedCommands(Collection<String> blockedCommands){
            this.blockedCommands = blockedCommands;
            return this;
        }

        public Builder blockedCommands(String... blockedCommands){
            this.blockedCommands = Arrays.asList(blockedCommands);
            return this;
        }

        public CommandCheck build(){
            return blockedCommands == null ? new CommandCheck() : new CommandCheck(blockedCommands);
        }
    }
}
