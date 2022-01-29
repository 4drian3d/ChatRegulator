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
public class FloodCheck extends AbstractCheck {
    // Credit: https://github.com/2lstudios-mc/ChatSentinel/blob/master/src/main/resources/config.yml#L91
    // (\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)
    private static String stringPattern = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private static Pattern floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * Update the Flood pattern based in the configuration
     */
    public static void setFloodRegex(){
        stringPattern = "(\\w)\\1{" + Configuration.getConfig().getFloodConfig().getLimit() + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    /**
     * Update the Flood pattern based on a custom limit
     * @param limit the custom limit
     */
    public static void setFloodRegex(int limit){
        stringPattern = "(\\w)\\1{" + limit + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public CompletableFuture<? extends Result> check(@NotNull String message){
        super.string = Objects.requireNonNull(message);
        Matcher matcher = floodPattern.matcher(message);
        boolean result = matcher.find();
        return CompletableFuture.completedFuture(new PatternReplaceableResult(message, result, floodPattern, matcher){
            @Override
            public String replaceInfraction(){
                return matcher.replaceAll("");
            }
        });
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.FLOOD;
    }
}
