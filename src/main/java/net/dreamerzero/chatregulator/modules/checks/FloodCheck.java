package net.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.config.Configuration;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck extends AbstractCheck {

    /**
     * Create a new flood test
     */
    public FloodCheck(){
        super.pattern = "(\\w)\\1{"+Configuration.getConfig().getFloodConfig().getLimit()+",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
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
