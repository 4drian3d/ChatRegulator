package io.github._4drian3d.chatregulator.api.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;
import io.github._4drian3d.chatregulator.api.result.PatternReplaceableResult;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public final class FloodCheck implements ICheck {
    private static final String STANDARD_PATTERN = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private final static Pattern floodPattern = Pattern.compile(STANDARD_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private final Pattern pattern;

    private FloodCheck(Pattern pattern){
        this.pattern = pattern;
    }

    /**
     * {@inheritDoc}
     *
     * @return a {@link PatternReplaceableResult} with the Result of the check
     */
    @Override
    public @NotNull CompletableFuture<Result> check(@NotNull final String string){
        return CompletableFuture.supplyAsync(() -> {
            final Matcher matcher = pattern.matcher(Objects.requireNonNull(string));
            return new PatternReplaceableResult(string, matcher.find(), pattern, matcher){
                @Override
                public String replaceInfraction() {
                    return matcher.replaceAll(match -> Character.toString(match.group().charAt(0)));
                }
            };
        });
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.FLOOD;
    }

    public static FloodCheck.Builder builder(){
        return new FloodCheck.Builder();
    }

    /**Flood Check Builder */
    public static class Builder implements AbstractBuilder<FloodCheck> {
        private Pattern pattern;

        Builder(){}

        /**
         * Set the pattern
         * @param pattern the new pattern
         * @return the builder itself
         */
        public Builder pattern(Pattern pattern){
            this.pattern = pattern;
            return this;
        }

        public Builder limit(int limit){
            if(pattern == null){
                this.pattern = Pattern.compile(STANDARD_PATTERN.replace(5+"", limit+""), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
            return this;
        }

        @Override
        public @NotNull FloodCheck build(){
            return pattern == null
                    ? new FloodCheck(floodPattern)
                    : new FloodCheck(this.pattern);
        }
    }
}
