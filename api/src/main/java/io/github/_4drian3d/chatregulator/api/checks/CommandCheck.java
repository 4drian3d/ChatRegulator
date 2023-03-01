package io.github._4drian3d.chatregulator.api.checks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import io.github._4drian3d.chatregulator.api.utils.Commands;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Check for verification of executed commands
 */
public final class CommandCheck implements ICheck {
    private final Collection<String> blockedCommands;

    private CommandCheck(Collection<String> blockedCommands){
        this.blockedCommands = blockedCommands;
    }

    /**
     * {@inheritDoc}
     *
     * @return A Result with the string with the command blocked and if the check was successful
     */
    @Override
    public @NotNull CompletableFuture<Result> check(@NotNull String string) {
        return CompletableFuture.supplyAsync(() -> {
            for (final String blockedCommand : blockedCommands){
                if (Commands.isStartingString(string, blockedCommand)) {
                    return new Result(string, true);
                }
            }
            return new Result(string, false);
        });
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.BCOMMAND;
    }

    /**
     * Create a new builder for CommandCheck
     * @return a new CommandCheck.Builder
     */
    public static CommandCheck.Builder builder(){
        return new CommandCheck.Builder();
    }

    /**Command Check Builder */
    public static class Builder implements AbstractBuilder<CommandCheck> {
        private Collection<String> blockedCommands;

        private Builder(){}

        /**
         * Set the blocked commands
         * @param blockedCommands the blocked commands
         * @return this
         */
        public Builder blockedCommands(Collection<String> blockedCommands){
            this.blockedCommands = blockedCommands;
            return this;
        }

        /**
         * Set the blocked commands
         * @param blockedCommands the blocked commands
         * @return this
         */
        public Builder blockedCommands(String... blockedCommands){
            if (this.blockedCommands == null) {
                this.blockedCommands = new HashSet<>(Arrays.asList(blockedCommands));
            } else {
                Collections.addAll(this.blockedCommands, blockedCommands);
            }
            
            return this;
        }

        /**
         * Adds a command to the blocked commands
         * @param command the command to add
         * @return this
         */
        public Builder addBlockedCommand(String command){
            if(this.blockedCommands == null)
                this.blockedCommands = new HashSet<>();
            this.blockedCommands.add(command);
            return this;
        }

        /**
         * Build a new CommandCheck with the Builder values
         * @return a new CommandCheck
         */
        @Override
        public @NotNull CommandCheck build(){
            return new CommandCheck(blockedCommands == null ? Collections.emptyList() : blockedCommands);
        }
    }
}
