package net.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Pattern;

import de.leonhard.storage.Yaml;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck extends AbstractCheck {

    /**
     * Create a new flood test
     * @param config plugin configuration
     */
    public FloodCheck(Yaml config){
        super.pattern = "(\\w)\\1{"+config.getString("flood.limit")+",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    }

    @Override
    public void check(String message){
        super.string = message;

        super.matcher = Pattern.compile(pattern).matcher(message);
        super.detected = matcher.lookingAt();
    }

    public String replaceInfraction(){
        return super.matcher.replaceAll("");
    }
}
