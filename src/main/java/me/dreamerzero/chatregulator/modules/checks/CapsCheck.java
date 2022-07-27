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
public final class CapsCheck implements ICheck {
    private final int limit;

    private CapsCheck(int limit){
        this.limit = limit;
    }

    /**
     * {@inheritDoc}
     *
     * @return A {@link ReplaceableResult} if the check was succesfully or a {@link Result} if not
     */
    @Override
    public CompletableFuture<Result> check(final @NotNull String string) {
        return CompletableFuture.supplyAsync(() -> Objects.requireNonNull(string)
            .chars()
            .filter(Character::isUpperCase)
            .count() >= this.limit
            ? new ReplaceableResult(string, true){
                @Override
                public String replaceInfraction(){
                    return string.toLowerCase(Locale.ROOT);
                }
            }
            : new Result(string, false));
    }

    public static CompletableFuture<Result> createCheck(String string, Configuration config){
        return new CapsCheck(config.getCapsConfig().limit()).check(string);
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
            return new CapsCheck(limit == 0 ? 5 : limit);
        }
    }
}
