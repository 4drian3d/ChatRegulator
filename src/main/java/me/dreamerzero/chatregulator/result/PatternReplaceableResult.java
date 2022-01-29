package me.dreamerzero.chatregulator.result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternReplaceableResult extends PatternResult implements IReplaceable {

    public PatternReplaceableResult(String infractionString, boolean infricted, Pattern pattern, Matcher matcher) {
        super(infractionString, infricted, pattern, matcher);
    }
}
