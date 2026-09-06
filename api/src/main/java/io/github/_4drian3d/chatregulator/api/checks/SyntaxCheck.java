package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Check that validates command syntax by checking namespace prefixes.
 */
public final class SyntaxCheck implements Check {
    private final Collection<String> allowedCommands;

    SyntaxCheck(Collection<String> allowedCommands) {
        this.allowedCommands = allowedCommands;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final String command = Commands.getFirstArgument(requireNonNull(string));
        final int index = command.indexOf(':');
        if (index == -1 || allowedCommands.contains(command.substring(0, index))) {
            return CheckResult.allowed();
        }

        return CheckResult.denied(type());
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SYNTAX;
    }

    /**
     * Creates a new Builder
     *
     * @return a new SyntaxCheck Builder
     */
    public static Builder builder(){
        return new Builder();
    }

    /**
     * Syntax Check Builder
     */
    public static class Builder implements AbstractBuilder<SyntaxCheck> {
        private final Set<String> allowedCommands = new HashSet<>();

        private Builder() {}

        /**
         * Add command namespaces to the list of allowed command prefixes.
         * <br>
         * Commands with these namespace prefixes (before the colon) will be allowed
         * to pass the check. Can be called multiple times to accumulate namespaces.
         *
         * @param commands the allowed command namespaces (e.g., "minecraft", "mymod")
         * @return this builder for chaining
         * @throws NullPointerException if commands or any command is null
         */
        public Builder allowedCommands(final @NotNull Collection<@NotNull String> commands){
            this.allowedCommands.addAll(commands);
            return this;
        }

        @Override
        public @NotNull SyntaxCheck build(){
            return new SyntaxCheck(allowedCommands);
        }
    }
}
