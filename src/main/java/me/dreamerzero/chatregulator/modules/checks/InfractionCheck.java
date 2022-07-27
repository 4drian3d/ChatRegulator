package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;
import me.dreamerzero.chatregulator.result.MultiPatternReplaceableResult;
import me.dreamerzero.chatregulator.result.PatternResult;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Utilities for the detection of restringed words
 */
public final class InfractionCheck implements ICheck {
    private final Pattern[] blockedWords;
    private final boolean blockable;

    private InfractionCheck(boolean blockable, Pattern... blockedWords){
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
    public CompletableFuture<Result> check(final @NotNull String string){
        return CompletableFuture.supplyAsync(() -> {
            final List<Matcher> matchers = new ArrayList<>(5);
            final List<Pattern> patterns = new ArrayList<>(5);
            boolean detected = false;
            for (final Pattern pattern : blockedWords) {
                final Matcher match = pattern.matcher(string);
                if(match.find()){
                    detected = true;
                    if (blockable) {
                        return new PatternResult(match.group(), blockable, pattern, match);
                    }
                    matchers.add(match);
                    patterns.add(pattern);
                }
            }
            return detected
                ? new MultiPatternReplaceableResult(string, true, matchers.toArray(new Matcher[0])){
                    @Override
                    public String replaceInfraction(){
                        String original = string;
                        for (final Pattern pattern : patterns) {
                            original = pattern.matcher(original)
                                .replaceAll(InfractionCheck::generateReplacement);
                        }
                        return original;
                    }
                }
                : new Result(string, false);
        });
    }

    static String generateReplacement(final MatchResult result) {
        final int size = result.group().length() / 2;
        final StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append('*');
        }
        return builder.toString();
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGULAR;
    }

    public static @NotNull CompletableFuture<Result> createCheck(String string, ChatRegulator plugin){
        return new InfractionCheck(
            plugin.getConfig().getInfractionsConfig().isBlockable(),
            plugin.getBlacklist().getBlockedPatterns()
        ).check(string);
    }

    public static @NotNull InfractionCheck.Builder builder(){
        return new InfractionCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<InfractionCheck> {
        private Set<Pattern> blockedWords;
        private boolean replaceable;
        private boolean edited = false;

        Builder(){}

        public Builder blockedPattern(Collection<Pattern> patterns){
            if(this.blockedWords == null) {
                this.blockedWords = new LinkedHashSet<>(patterns);
            } else {
                this.blockedWords.addAll(patterns);
            }
            return this;
        }

        public Builder blockedPatterns(Pattern... patterns){
            if(this.blockedWords == null) {
                this.blockedWords = new LinkedHashSet<>(List.of(patterns));
            } else {
                Collections.addAll(this.blockedWords, patterns);
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
                this.blockedWords = Collections.emptySet();
            }
            if(!edited){
                this.replaceable = false;
            }
            return new InfractionCheck(!replaceable, blockedWords.toArray(new Pattern[0]));
        }
    }
}
