package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Check for verification of executed commands
 */
public final class CommandCheck implements ICheck {
    private final Collection<String> blockedCommands;

    private CommandCheck(Collection<String> blockedCommands){
        this.blockedCommands = blockedCommands;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        for (final String blockedCommand : blockedCommands){
            if (Commands.isStartingString(string, blockedCommand)) {
                return CheckResult.denied(type());
            }
        }
        return CheckResult.allowed();
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.BLOCKED_COMMAND;
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
            if (this.blockedCommands == null) this.blockedCommands = new HashSet<>();

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
