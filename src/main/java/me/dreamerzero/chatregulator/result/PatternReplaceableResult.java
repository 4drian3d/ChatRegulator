package me.dreamerzero.chatregulator.result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

/**Result derived from a single {@link Pattern} and that its violation can be replaced */
public class PatternReplaceableResult extends PatternResult implements IReplaceable {

    /**
     * Creates a new PatternReplaceableResult
     * @param infractionString the infraction string
     * @param infricted if it was infricted
     * @param pattern the pattern
     * @param matcher the matcher
     */
    public PatternReplaceableResult(String infractionString, boolean infricted, Pattern pattern, Matcher matcher) {
        super(infractionString, infricted, pattern, matcher);
    }

    @Override
    public @Nullable String replaceInfraction() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
