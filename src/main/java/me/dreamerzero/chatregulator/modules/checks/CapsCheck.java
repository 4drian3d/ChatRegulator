package me.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Check for compliance with uppercase character limit in a string
 */
public class CapsCheck implements ICheck {
    private final int limit;

    private CapsCheck(){
        this.limit = Configuration.getConfig().getCapsConfig().limit();
    }

    private CapsCheck(int limit){
        this.limit = limit;
    }

    @Override
    public CompletableFuture<Result> check(@NotNull String string) {
        char[] chararray = Objects.requireNonNull(string).toCharArray();
        int capcount = 0;
        for(char c : chararray){
            if(Character.isUpperCase(c)) capcount++;
        }

        return CompletableFuture.completedFuture(capcount >= this.limit
            ? new ReplaceableResult(string, true){
                @Override
                public String replaceInfraction(){
                    return string.toLowerCase(Locale.ROOT);
                }
            }
            : new Result(string, false));
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new CapsCheck().check(string);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.CAPS;
    }

    /**
     * Create a builder
     * @return a new CapsCheck.Builder
     */
    public static CapsCheck.Builder builder(){
        return new CapsCheck.Builder();
    }

    /**Caps Check Builder */
    public static class Builder implements AbstractBuilder<CapsCheck> {
        private int limit;
        Builder(){}

        /**
         * Set the new caps limit
         * @param limit the new limit
         * @return this
         */
        public Builder limit(int limit){
            this.limit = limit;
            return this;
        }

        @Override
        public CapsCheck build(){
            return limit == 0
                ? new CapsCheck()
                : new CapsCheck(limit);
        }
    }
}
