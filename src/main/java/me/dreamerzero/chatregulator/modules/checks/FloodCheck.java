package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;
import me.dreamerzero.chatregulator.result.PatternReplaceableResult;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public final class FloodCheck implements ICheck {
    // Credit: https://github.com/2lstudios-mc/ChatSentinel/blob/master/src/main/resources/config.yml#L91
    // (\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)
    private static final String STANDARD_PATTERN = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private static Pattern floodPattern = Pattern.compile(STANDARD_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private final Pattern realPattern;

    private FloodCheck(){
        this(floodPattern);
    }

    private FloodCheck(Pattern pattern){
        this.realPattern = pattern;
    }

    /**
     * Update the Flood pattern based in the configuration
     */
    public static void setFloodRegex(){
        floodPattern = Pattern.compile(STANDARD_PATTERN.replace(5+"", Configuration.getConfig().getFloodConfig().getLimit()+""), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public CompletableFuture<Result> check(final @NotNull String string){
        final Matcher matcher = realPattern.matcher(Objects.requireNonNull(string));
        return CompletableFuture.completedFuture(new PatternReplaceableResult(string, matcher.find(), realPattern, matcher){
            @Override
            public String replaceInfraction(){
                return matcher.replaceAll(match -> Character.toString(match.group().charAt(0)));
            }
        });
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new FloodCheck().check(string);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.FLOOD;
    }

    public static FloodCheck.Builder builder(){
        return new FloodCheck.Builder();
    }

    /**Floood Check Builder */
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
            return pattern == null ? new FloodCheck() : new FloodCheck(pattern);
        }
    }
}
