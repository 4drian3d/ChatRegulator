package net.dreamerzero.chatregulator.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.dreamerzero.chatregulator.Regulator;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodUtils {
    /**
     * Checks if the delivered string contains a flood type violation
     * @return if the string contains a flood violation
     */
    public static boolean isFlood(String message){
        String floodPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)"
            .replace("<l>", Regulator.getConfig().getString("flood.limit"));

        Matcher floodMatch = Pattern.compile(floodPattern).matcher(message);
        return floodMatch.find();
    }

    /**
     * Get the Flood pattern
     * @return the flood pattern
     */
    public static String getFloodPattern(){
        return "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    }
}
