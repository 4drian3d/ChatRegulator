package me.dreamerzero.chatregulator.result;

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
}
