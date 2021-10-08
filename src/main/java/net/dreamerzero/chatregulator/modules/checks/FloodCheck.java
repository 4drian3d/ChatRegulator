package net.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck extends Check {

    /**
     * Create a new flood test
     * @param config plugin configuration
     */
    public FloodCheck(Yaml config){
        super.pattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)"
        .replace("<l>", config.getString("flood.limit"));
    }

    @Override
    public void check(String message){
        super.string = message;

        super.matcher = Pattern.compile(pattern).matcher(message);
        super.detected = matcher.lookingAt();
    }
}
