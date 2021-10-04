package net.dreamerzero.chatregulator.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionCheck {
    private Yaml blacklist;
    private String pattern;
    private Matcher matcher;
    private String string;
    public InfractionCheck(Yaml blacklist){
        this.blacklist = blacklist;
    }
    /**
     * Check if the delivered string contains any restringed words.
     * @param string the message to be reviewed for infringement
     * @return if the string contains any forbidden words
     */
    public boolean isInfraction(String string){
        List<String> blockedWords = blacklist.getStringList("blocked-words");
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(string);
            this.string = string;
            if(match.lookingAt()){
                pattern = blockedWord;
                matcher = match;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the regex pattern by which the word was detected.
     * @param string the string to check
     * @return the regex pattern by which the string was detected
     */
    public String getPattern(){
        return this.pattern;
    }

    public String getInfractionWord(){
        return string.substring(matcher.start(), matcher.end());
    }
}
