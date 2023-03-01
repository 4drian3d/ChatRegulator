package io.github._4drian3d.chatregulator.api.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.github._4drian3d.chatregulator.api.utils.Commands;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.Result;

public final class SyntaxCheck implements ICheck {
    private static final SyntaxCheck cachedCheck = new SyntaxCheck();

    /**
     * {@inheritDoc}
     *
     * @return A Result with the command executed
     */
    @Override
    public @NotNull CompletableFuture<Result> check(final @NotNull String string) {
        return CompletableFuture.supplyAsync(() -> {
            final String command = Commands.getFirstArgument(Objects.requireNonNull(string));
            return new Result(command, command.indexOf(':') != -1);
        });
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SYNTAX;
    }

    public static CompletableFuture<Result> createCheck(String string){
        return cachedCheck.check(string);
    }
    private SyntaxCheck(){}
}
