package io.github._4drian3d.chatregulator.api.result;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

/**Result of any Check */
public class Result {
    private final boolean inflicted;
    private final String infractionString;

    /**
     * Creates a new Result
     * @param infractionString the infraction string
     * @param inflicted if it was inflicted
     */
    public Result(String infractionString, boolean inflicted){
        this.inflicted = inflicted;
        this.infractionString = infractionString;
    }
    /**
     * Check the detection result
     * @return the result
     */
    public boolean isInfraction(){
        return this.inflicted;
    }

    /**
     * Get the string involved in the detection
     * @return the infraction string
     */
    public @Nullable String getInfractionString(){
        return this.infractionString;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Result that)) return false;
        return that.infractionString.equals(this.infractionString)
            && that.inflicted == this.inflicted;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.infractionString, this.inflicted);
    }

    @Override
    public String toString(){
        return "Result[infractionString="+this.infractionString+",infricted="+this.inflicted +"]";
    }
}
