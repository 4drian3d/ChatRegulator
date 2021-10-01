package net.dreamerzero.chatregulator.modules;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.Regulator;

public class InfractionUtils {
    public static boolean isInfraction(String message){
        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(message);
            if(match.find()) return true;
        }
        return false;
    }

    public static String getPattern(String message){
        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(message);
            if(match.find()) return blockedWord;
        }
        return "No Pattern";
    }
}
