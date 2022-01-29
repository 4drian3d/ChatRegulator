package me.dreamerzero.chatregulator.modules.checks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks that work by using a {@link Pattern}
 */
public abstract class PatternCheck extends AbstractCheck {
    protected Pattern pattern;
    protected Matcher matcher;
}
