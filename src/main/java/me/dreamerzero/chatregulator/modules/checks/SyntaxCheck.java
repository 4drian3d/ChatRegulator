package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.CommandUtils;

public final class SyntaxCheck implements ICheck{
    private static final SyntaxCheck cachedCheck = new SyntaxCheck();

    /**
     * {@inheritDoc}
     *
     * @return A Result with the command executed
     */
    @Override
    public CompletableFuture<Result> check(final @NotNull String string) {
        final String command = CommandUtils.getFirstArgument(Objects.requireNonNull(string));
        return CompletableFuture.completedFuture(new Result(command, command.indexOf(':') != -1));
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
