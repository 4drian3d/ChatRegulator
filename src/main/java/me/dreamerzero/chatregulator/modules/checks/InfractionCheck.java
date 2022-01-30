package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.Arrays;
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
import me.dreamerzero.chatregulator.result.PatternResult;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck implements ICheck {
    private final Collection<String> blockedWords;
    private final boolean blockable;

    private InfractionCheck(){
        this.blockedWords  = Configuration.getBlacklist().getBlockedWord();
        this.blockable = Configuration.getConfig().getInfractionsConfig().isBlockable();
    }

    InfractionCheck(boolean blockable, Collection<String> blockedWords){
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
     * @see {@link ICheck}
     */
    @Override
    public CompletableFuture<Result> check(@NotNull String string){
        final List<Pattern> patterns = new ArrayList<>();
        boolean detected = false;
        for (String blockedWord : blockedWords){
            Pattern wordpattern = Pattern.compile(blockedWord, Pattern.CASE_INSENSITIVE);
            Matcher match = wordpattern.matcher(string);
            if(match.find()){
                detected = true;
                if(blockable) {
                    return CompletableFuture.completedFuture(new PatternResult(match.group(), true, wordpattern, match));
                }
                patterns.add(wordpattern);
            }
        }
        if(detected){
            return CompletableFuture.completedFuture(new ReplaceableResult(patterns.toString(), true){
                @Override
                public String replaceInfraction(){
                    String original = string;
                    for(Pattern pattern : patterns){
                        original = pattern.matcher(original).replaceAll("***");
                    }
                    return original;
                }
            });
        }

        return CompletableFuture.completedFuture(new Result(string, false));
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGULAR;
    }

    public static CompletableFuture<Result> createCheck(String string){
        return new InfractionCheck().check(string);
    }

    public static InfractionCheck.Builder builder(){
        return new InfractionCheck.Builder();
    }

    public static class Builder{
        private Collection<String> blockedWords;
        private boolean replaceable;
        private boolean edited = false;

        Builder(){}

        public Builder blockedStrings(Collection<String> strings){
            this.blockedWords = strings;
            return this;
        }

        public Builder blockedStrings(String... strings){
            this.blockedWords = Arrays.asList(strings);
            return this;
        }

        public Builder replaceable(boolean replaceable){
            this.replaceable = replaceable;
            this.edited = true;
            return this;
        }

        public InfractionCheck build(){
            if(blockedWords == null){
                blockedWords = Configuration.getBlacklist().getBlockedWord();
            }
            if(!edited){
                this.replaceable = Configuration.getConfig().getInfractionsConfig().getControlType() == ControlType.REPLACE;
            }
            return new InfractionCheck(!replaceable, blockedWords);
        }
    }
}
