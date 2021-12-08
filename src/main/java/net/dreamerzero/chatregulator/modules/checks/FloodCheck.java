package net.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Pattern;

import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Utilities for detecting incoherent messages containing floods
 */
public class FloodCheck extends AbstractCheck {
    // Credit: https://github.com/2lstudios-mc/ChatSentinel/blob/master/src/main/resources/config.yml#L91
    // (\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)
    private static String stringPattern = "(\\w)\\1{5,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
    private static Pattern floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * Create a new flood test
     */
    public FloodCheck(){
        super.pattern = stringPattern;
    }

    /**
     * Update the Flood pattern based in the configuration
     */
    public static void setFloodRegex(){
        stringPattern = "(\\w)\\1{" + Configuration.getConfig().getFloodConfig().getLimit() + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    /**
     * Update the Flood pattern based on a custom limit
     * @param limit the custom limit
     */
    public static void setFloodRegex(int limit){
        stringPattern = "(\\w)\\1{" + limit + ",}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)";
        floodPattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
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

    @Override
    public InfractionType type() {
        return InfractionType.FLOOD;
    }
}
