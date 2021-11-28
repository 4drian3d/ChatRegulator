package net.dreamerzero.chatregulator.modules.checks;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.config.Configuration;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck extends AbstractCheck {
    private Set<String> blockedWords;
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
            Matcher match = Pattern.compile(blockedWord).matcher(string);
            if(match.find()){
                super.pattern = blockedWord;
                super.matcher = match;
                super.detected = true;
                return;
            }
        }
        super.detected = false;
    }

    public String replaceInfraction(){
        return super.matcher.replaceAll("***");
    }
}
