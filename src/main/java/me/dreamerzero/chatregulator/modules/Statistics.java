package me.dreamerzero.chatregulator.modules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Manages the plugin's internal statistics
 */
public final class Statistics {
    /**
     * Global Spam warning count
     */
    private int spamCount;
    /**
     * Global Flood warning count
     */
    private int floodCount;
    /**
     * Global Regular Infractions warning count
     */
    private int regularCount;

    /**
     * Global commands blocked executed
     */
    private int commandCount;

    /**
     * Global Unicode caracters count
     */
    private int unicodeViolations;

    /**
     * Global Caps violations count
     */
    private int capsViolations;

    /**
     * Syntax Violations count
     */
    private int syntaxViolations;

    /**
     * Global Violations count
     */
    private int globalViolations;

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addViolationCount(@NotNull InfractionType type){
        switch(type){
            case SPAM -> this.spamCount++;
            case FLOOD -> this.floodCount++;
            case REGULAR -> this.regularCount++;
            case BCOMMAND -> this.commandCount++;
            case UNICODE -> this.unicodeViolations++;
            case CAPS -> this.capsViolations++;
            case SYNTAX -> this.syntaxViolations++;
            case NONE -> this.globalViolations++;
        }
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

    void resetViolationCount(){
        this.spamCount = 0;
        this.floodCount = 0;
        this.regularCount = 0;
        this.commandCount = 0;
        this.unicodeViolations = 0;
        this.capsViolations = 0;
        this.syntaxViolations = 0;
        this.globalViolations = 0;
    }

    void setViolationCount(InfractionType type, int amount){
        switch(type){
            case SPAM -> this.spamCount = amount;
            case FLOOD -> this.floodCount = amount;
            case REGULAR -> this.regularCount = amount;
            case BCOMMAND -> this.commandCount = amount;
            case UNICODE -> this.unicodeViolations = amount;
            case CAPS -> this.capsViolations = amount;
            case SYNTAX -> this.syntaxViolations = amount;
            case NONE -> {
                this.globalViolations = this.syntaxViolations +
                    this.spamCount + this.floodCount +
                    this.regularCount + this.commandCount +
                    this.unicodeViolations + this.capsViolations;
            }
        }
        
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
