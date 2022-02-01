package me.dreamerzero.chatregulator.result;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;

/**A Result derived from a specific pattern */
public class PatternResult extends Result {
    private final Matcher matcher;
    private final Pattern pattern;

    /**
     * Creates a new PatternResult
     * @param infractionString the infraction string
     * @param infricted if it was infricted
     * @param pattern the pattern
     * @param matcher the matcher
     */
    public PatternResult(String infractionString, boolean infricted, Pattern pattern, Matcher matcher){
        super(infractionString, infricted);
        this.matcher = matcher;
        this.pattern = pattern;
    }

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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        PatternResult that = (PatternResult) o;
        return that.getInfractionString().equals(this.getInfractionString()) && that.isInfraction() == this.isInfraction();
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getInfractionString(), this.isInfraction());
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        if(pattern != null) builder.append(pattern.toString());
        if(matcher != null) builder.append(matcher.toString());
        String string = builder.toString();
        if(string.isBlank()){
            return super.toString();
        }
        return "PatternResult["+ super.toString() + string + "]";
    }
}
