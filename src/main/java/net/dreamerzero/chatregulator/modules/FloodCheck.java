package net.dreamerzero.chatregulator.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck {
    private Matcher matcher;
    private String floodPattern;
    private String string;
    public FloodCheck(Yaml config){
        floodPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)"
        .replace("<l>", config.getString("flood.limit"));
    }
    /**
     * Checks if the delivered string contains a flood type violation
     * @return if the string contains a flood violation
     */
    public boolean isFlood(String message){
        string = message;

        matcher = Pattern.compile(floodPattern).matcher(message);
        return matcher.lookingAt();
    }

    public String getInfractionWord(){
        return string.substring(matcher.start(), matcher.end());
    }

    /**
     * Get the Flood pattern
     * @return the flood pattern
     */
    public String getFloodPattern(){
        return this.floodPattern;
    }
}
