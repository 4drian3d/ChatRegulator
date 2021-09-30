package net.dreamerzero.chatregulator.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import net.dreamerzero.chatregulator.Regulator;

public class FloodUtils {
    public static boolean isFlood(String message){
        String floodPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)"
            .replace("<l>", Regulator.getConfig().getString("flood.limit"));

        Matcher floodMatch = Pattern.compile(floodPattern).matcher(message);
        return floodMatch.find();
    }

    public static String getFloodPattern(){
        return "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    }
}
