package me.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Matcher;

import me.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Base class of the checks used in the plugin
 */
public abstract class AbstractCheck {
    protected boolean detected;
    protected String pattern;
    protected Matcher matcher;
    protected String string;

    /**
     * Check if the delivered string contains any infraction
     * @param message the message to check
     */
    public abstract void check(String message);

    /**
     * Get the InfractionType
     * @return the infraction type
     */
    public abstract InfractionType type();

    /**
     * Check the detection result
     * @return the result
     */
    public boolean isInfraction(){
        return this.detected;
    }

    /**
     * Gets the regex pattern by which the word was detected.
     * @return the regex pattern by which the string was detected
     */
    public String getPattern(){
        return this.pattern;
    }

    /**
     * Get the word involved in the detection
     * @return the infraction word
     */
    public String getInfractionWord(){
        return this.string.substring(matcher.start(), matcher.end());
    }

}
