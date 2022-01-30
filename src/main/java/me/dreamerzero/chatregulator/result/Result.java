package me.dreamerzero.chatregulator.result;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

public class Result {
    private final boolean infricted;
    private final String infractionString;

    public Result(String infractionString, boolean infricted){
        this.infricted = infricted;
        this.infractionString = infractionString;
    }
    /**
     * Check the detection result
     * @return the result
     */
    public boolean isInfraction(){
        return this.infricted;
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
        if(o == null || o.getClass() != this.getClass()) return false;
        Result that = (Result) o;
        return that.infractionString.equals(this.infractionString) && that.infricted == this.infricted;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.infractionString, this.infricted);
    }

    @Override
    public String toString(){
        return "Result[infractionString="+this.infractionString+",infricted="+this.infricted+"]";
    }
}
