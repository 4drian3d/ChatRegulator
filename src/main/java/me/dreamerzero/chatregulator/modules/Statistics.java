package me.dreamerzero.chatregulator.modules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Manages the plugin's internal statistics
 */
public final class Statistics {
    private int spamCount;
    private int floodCount;
    private int regularCount;
    private int commandCount;
    private int unicodeViolations;
    private int capsViolations;
    private int syntaxViolations;
    private int globalViolations;

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addViolationCount(@NotNull InfractionType type){
        switch(type){
            case SPAM -> ++this.spamCount;
            case FLOOD -> ++this.floodCount;
            case REGULAR -> ++this.regularCount;
            case BCOMMAND -> ++this.commandCount;
            case UNICODE -> ++this.unicodeViolations;
            case CAPS -> ++this.capsViolations;
            case SYNTAX -> ++this.syntaxViolations;
            case NONE -> {}
        }
        ++this.globalViolations;
    }

    /**
     * Obtain the number of infractions of some type
     * @param type the infraction type
     * @return count of the respective infraction type
     */
    public int getViolationCount(@NotNull InfractionType type){
        return switch(type){
            case SPAM -> this.spamCount;
            case FLOOD -> this.floodCount;
            case REGULAR -> this.regularCount;
            case BCOMMAND -> this.commandCount;
            case UNICODE -> this.unicodeViolations;
            case CAPS -> this.capsViolations;
            case SYNTAX -> this.syntaxViolations;
            case NONE -> this.globalViolations;
        };
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof final Statistics that)) {
            return false;
        }

        return this.globalViolations == that.globalViolations;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.globalViolations);
    }

    @Override
    public String toString(){
        return "Statistics["
            +"regular="+this.regularCount
            +",flood="+this.floodCount
            +",spam="+this.spamCount
            +",caps="+this.capsViolations
            +",command="+this.commandCount
            +",unicode="+this.unicodeViolations
            +",syntax="+this.syntaxViolations
            +"]";
    }
}
