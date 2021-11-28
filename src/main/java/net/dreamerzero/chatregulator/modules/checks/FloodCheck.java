package net.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.config.Configuration;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck extends AbstractCheck {
    // Credit: https://github.com/2lstudios-mc/ChatSentinel/blob/master/src/main/resources/config.yml#L91
    // (\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)
    private static String stringpattern = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private static Pattern floodPattern = Pattern.compile(stringpattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * Create a new flood test
     */
    public FloodCheck(){
        super.pattern = stringpattern;
    }

    public static void setFloodRegex(){
        stringpattern = "(\\w)\\1{" + Configuration.getConfig().getFloodConfig().getLimit() + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringpattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    public static void setFloodRegex(int limit){
        stringpattern = "(\\w)\\1{" + limit + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringpattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    @Override
    public void check(String message){
        super.string = message;

        super.matcher = floodPattern.matcher(message);
        super.detected = matcher.find();
    }

    public String replaceInfraction(){
        return super.matcher.replaceAll("");
    }
}
