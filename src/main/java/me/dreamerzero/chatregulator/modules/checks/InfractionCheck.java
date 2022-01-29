package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.result.PatternReplaceableResult;
import me.dreamerzero.chatregulator.result.PatternResult;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck extends PatternCheck {
    private final Set<String> blockedWords;
    private final List<Pattern> patterns = new ArrayList<>();
    private final boolean blockable;
    private String originalString;
    /**
     * Create a new infringement test
     */
    public InfractionCheck(){
        this.blockedWords  = Configuration.getBlacklist().getBlockedWord();
        this.blockable = Configuration.getConfig().getInfractionsConfig().isBlockable();
    }

    @VisibleForTesting InfractionCheck(boolean test){
        this.blockedWords  = Configuration.getBlacklist().getBlockedWord();
        this.blockable = false;
    }

    @Override
    public CompletableFuture<? extends Result> check(@NotNull String string){
        originalString = string;
        for (String blockedWord : blockedWords){
            Pattern wordpattern = Pattern.compile(blockedWord, Pattern.CASE_INSENSITIVE);
            Matcher match = wordpattern.matcher(string);
            if(match.find()){
                super.matcher = match;
                super.detected = true;
                super.pattern = wordpattern;
                if(blockable) {
                    return CompletableFuture.completedFuture(new PatternResult(match.group(), true, pattern, matcher));
                }
                patterns.add(wordpattern);
            }
        }
        if(detected){
            return CompletableFuture.completedFuture(new PatternReplaceableResult(patterns.toString(), true, pattern, matcher){
                @Override
                public String replaceInfraction(){
                    String original = originalString;
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
}
