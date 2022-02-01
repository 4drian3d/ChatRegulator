package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.result.PatternReplaceableResult;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck implements ICheck {
    // Credit: https://github.com/2lstudios-mc/ChatSentinel/blob/master/src/main/resources/config.yml#L91
    // (\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)
    private static final String stringPattern = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private static Pattern floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private final Pattern realPattern;

    static {
        floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

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
        floodPattern = Pattern.compile(stringPattern.replace(5+"", Configuration.getConfig().getFloodConfig().getLimit()+""), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public CompletableFuture<Result> check(@NotNull final String string){
        Matcher matcher = realPattern.matcher(Objects.requireNonNull(string));
        boolean result = matcher.find();
        return CompletableFuture.completedFuture(new PatternReplaceableResult(string, result, realPattern, matcher){
            @Override
            public String replaceInfraction(){
                return matcher.replaceAll("");
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

    public static class Builder{
        private Pattern pattern;

        Builder(){}

        public Builder pattern(Pattern pattern){
            this.pattern = pattern;
            return this;
        }

        public Builder limit(int limit){
            if(pattern == null){
                this.pattern = Pattern.compile(stringPattern.replace(5+"", limit+""), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            }
            return this;
        }

        public @NotNull FloodCheck build(){
            return pattern == null ? new FloodCheck() : new FloodCheck(pattern);
        }
    }
}
