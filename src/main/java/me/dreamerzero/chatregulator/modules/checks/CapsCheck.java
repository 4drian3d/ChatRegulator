package me.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Check for compliance with uppercase character limit in a string
 */
public class CapsCheck extends AbstractCheck {

    @Override
    public CompletableFuture<? extends Result> check(@NotNull String message) {
        super.string = Objects.requireNonNull(message);
        char[] chararray = message.toCharArray();
        int capcount = 0;
        for(char c : chararray){
            if(Character.isUpperCase(c)) capcount++;
        }

        if(capcount >= Configuration.getConfig().getCapsConfig().limit()){
            return CompletableFuture.completedFuture(new Result(message, true));
        }
        return CompletableFuture.completedFuture(new ReplaceableResult(message, false){
            @Override
            public String replaceInfraction(){
                return message.toLowerCase(Locale.ROOT);
            }
        });
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.CAPS;
    }
}
