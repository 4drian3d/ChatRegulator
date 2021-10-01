package net.dreamerzero.chatregulator.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.Regulator;

/**
 * Utilities for the detection of restringed words
 */
public class InfractionUtils {
    /**
     * Check if the delivered string contains any restringed words.
     * @param string the message to be reviewed for infringement
     * @return if the string contains any forbidden words
     */
    public static boolean isInfraction(String string){
        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(string);
            if(match.find()) return true;
        }
        return false;
    }

    /**
     * Gets the regex pattern by which the word was detected.
     * @param string the string to check
     * @return the regex pattern by which the string was detected
     */
    public static String getPattern(String string){
        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(string);
            if(match.find()) return blockedWord;
        }
        return "No Pattern";
    }
}
