package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;

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
    public void check(String string){
        originalString = string;
        for (String blockedWord : blockedWords){
            Pattern wordpattern = Pattern.compile(blockedWord, Pattern.CASE_INSENSITIVE);
            Matcher match = wordpattern.matcher(string);
            if(match.find()){
                super.matcher = match;
                super.detected = true;
                super.pattern = wordpattern;
                if(blockable) {
                    super.string = match.group();
                    return;
                }
                patterns.add(wordpattern);
            }
        }
        if(detected)
            super.string = patterns.toString();
    }

    @Override
    public @Nullable String replaceInfraction(){
        String original = originalString;
        for(Pattern pattern : patterns){
            original = pattern.matcher(original).replaceAll("***");
        }
        return original;
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGULAR;
    }
}
