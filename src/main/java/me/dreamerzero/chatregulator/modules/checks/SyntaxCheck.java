package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.CommandUtils;

public class SyntaxCheck implements ICheck{

    @Override
    public CompletableFuture<Result> check(@NotNull String string) {
        String command = CommandUtils.getFirstArgument(Objects.requireNonNull(string));
        return CompletableFuture.completedFuture(new Result(command, command.indexOf(':') != -1));
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SYNTAX;
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new SyntaxCheck().check(string);
    }
    private SyntaxCheck(){}
}
