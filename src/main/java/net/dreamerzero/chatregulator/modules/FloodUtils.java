package net.dreamerzero.chatregulator.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodUtils {
    private Yaml config;
    public FloodUtils(Yaml config){
        this.config = config;
    }
    /**
     * Checks if the delivered string contains a flood type violation
     * @return if the string contains a flood violation
     */
    public boolean isFlood(String message){
        String floodPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)"
            .replace("<l>", config.getString("flood.limit"));

        Matcher floodMatch = Pattern.compile(floodPattern).matcher(message);
        return floodMatch.find();
    }

    /**
     * Get the Flood pattern
     * @return the flood pattern
     */
    public String getFloodPattern(){
        return "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    }
}
