package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;
import me.dreamerzero.chatregulator.result.PatternResult;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Utilities for the detection of restringed words
 */
public final class InfractionCheck implements ICheck {
    private final Collection<Pattern> blockedWords;
    private final boolean blockable;

    private InfractionCheck(){
        this(Configuration.getConfig().getInfractionsConfig().isBlockable(), Configuration.getBlacklist().getBlockedPatterns());
    }

    private InfractionCheck(boolean blockable, Collection<Pattern> blockedWords){
        this.blockedWords = blockedWords;
        this.blockable = blockable;
    }

    /**
     * {@inheritDoc}
     * @param string the string
     * @return May return a {@link PatternResult} if the result was successful
     * and the check was set to only block the message.
     * Or a {@link ReplaceableResult} if the check was successful
     * and is configured to replace multiple violations.
     * Or a {@link Result} if the check was not successful
     * @see ICheck
     */
    @Override
    public CompletableFuture<Result> check(@NotNull final String string){
        final List<Pattern> patterns = new ArrayList<>();
        boolean detected = false;
        for (final Pattern pattern : blockedWords) {
            final Matcher match = pattern.matcher(string);
            if(match.find()){
                detected = true;
                if(blockable) {
                    return CompletableFuture.completedFuture(new PatternResult(match.group(), true, pattern, match));
                }
                patterns.add(pattern);
            }
        }
        return CompletableFuture.completedFuture(detected
            ? new ReplaceableResult(patterns.toString(), true){
                @Override
                public String replaceInfraction(){
                    String original = string;
                    for(Pattern pattern : patterns){
                        original = pattern.matcher(original).replaceAll("***");
                    }
                    return original;
                }
            }
            : new Result(string, false));
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGULAR;
    }

    public static @NotNull CompletableFuture<Result> createCheck(String string){
        return new InfractionCheck().check(string);
    }

    public static @NotNull InfractionCheck.Builder builder(){
        return new InfractionCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<InfractionCheck> {
        private List<Pattern> blockedWords;
        private boolean replaceable;
        private boolean edited = false;

        Builder(){}

        public Builder blockedPattern(Collection<Pattern> patterns){
            if(this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(patterns);
            } else {
                this.blockedWords.addAll(patterns);
            }
            return this;
        }

        public Builder blockedPatterns(Pattern... patterns){
            if(this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(List.of(patterns));
            } else {
                this.blockedWords.addAll(List.of(patterns));
            }
            return this;
        }

        public Builder replaceable(boolean replaceable){
            this.replaceable = replaceable;
            this.edited = true;
            return this;
        }

        @Override
        public InfractionCheck build(){
            if(this.blockedWords == null){
                this.blockedWords = new ArrayList<>(Configuration.getBlacklist().getBlockedPatterns());
            }
            if(!edited){
                this.replaceable = Configuration.getConfig().getInfractionsConfig().getControlType() == ControlType.REPLACE;
            }
            return new InfractionCheck(!replaceable, blockedWords);
        }
    }
}
