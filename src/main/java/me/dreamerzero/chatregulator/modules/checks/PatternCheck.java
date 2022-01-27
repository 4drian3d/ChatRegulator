package me.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

/**
 * Checks that work by using a {@link Pattern}
 */
public abstract class PatternCheck extends AbstractCheck implements ReplaceableCheck {
    protected Pattern pattern;
    protected Matcher matcher;
    /**
     * Gets the regex {@link Pattern} by which the word was detected.
     * @return the regex pattern by which the string was detected
     */
    public @Nullable Pattern getPattern(){
        return this.pattern;
    }

    /**
     * Obtain the corresponding {@link Matcher} of the detection performed
     *
     * It can be null if the check has not yet been performed,
     * if the check does not return a Matcher
     * or because the check gave a negative result
     * @return the matcher of this check
     */
    public @Nullable Matcher getMatcher(){
        return this.matcher;
    }
}
