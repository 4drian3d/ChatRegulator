package me.dreamerzero.chatregulator.result;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;

import org.jetbrains.annotations.Nullable;

public class MultiPatternReplaceableResult extends Result implements IReplaceable {
    private final Matcher[] matchers;

    /**
     * Creates a new MultiPatternReplaceableResult
     * @param infractionString the infraction string
     * @param infricted if it was infricted
     * @param matchers the matchers
     */
    public MultiPatternReplaceableResult(String infractionString, boolean infricted, Matcher[] matchers) {
        super(infractionString, infricted);
        this.matchers = matchers;
    }

    @Override
    public @Nullable String replaceInfraction() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Obtain the corresponding {@link Matcher}s of the detections performed
     *
     * It can be null if the check has not yet been performed,
     * if the check does not return a Matcher
     * or because the check gave a negative result
     * @return the matcher of this check
     */
    public Matcher[] getMatchers(){
        return this.matchers;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof PatternResult that)) return false;
        return Objects.equals(that.getInfractionString(), this.getInfractionString())
            && that.isInfraction() == this.isInfraction();
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getInfractionString(), matchers);
    }

    @Override
    public String toString(){
        String string = Arrays.toString(matchers);
        if(string.isBlank()){
            return super.toString();
        }
        return "PatternResult["+ super.toString() + string + "]";
    }
    
}
