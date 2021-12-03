package net.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.config.Configuration;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck extends AbstractCheck {
    private Set<String> blockedWords;
    private List<Pattern> patterns = new ArrayList<>();
    /**
     * Create a new infringement test
     */
    public InfractionCheck(){
        this.blockedWords  = Configuration.getBlacklist().getBlockedWord();
    }

    @Override
    public void check(String string){
        super.string = string;
        for (String blockedWord : blockedWords){
            Pattern wordpattern = Pattern.compile(blockedWord, Pattern.CASE_INSENSITIVE);
            Matcher match = wordpattern.matcher(string);
            if(match.find()){
                super.pattern = blockedWord;
                super.matcher = match;
                super.detected = true;
                patterns.add(wordpattern);
            }
        }
    }

    /**
     * Replace the infractions
     * @return the message without infractions
     */
    public String replaceInfractions(){
        String original = string;
        for(Pattern pattern : patterns){
            original = pattern.matcher(original).replaceAll("***");
        }
        return original;
    }
}
