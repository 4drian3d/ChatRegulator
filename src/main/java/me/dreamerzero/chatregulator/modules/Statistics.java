package me.dreamerzero.chatregulator.modules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Manages the plugin's internal statistics
 */
public final class Statistics {
    private static volatile Statistics statistics;
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
     * Global Violations count
     */
    private int globalViolations;

    private int syntaxViolations;

    /**
     * Get the global violation statistics
     * @return the global statistics
     */
    public static Statistics getStatistics(){
        Statistics result = statistics;
        if (result != null) {
            return result;
        }
        synchronized(Statistics.class) {
            if (statistics == null) {
                statistics = new Statistics();
            }
            return statistics;
        }
    }

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addViolationCount(@NotNull InfractionType type){
        switch(type){
            case SPAM: this.spamCount++; break;
            case FLOOD: this.floodCount++; break;
            case REGULAR: this.regularCount++; break;
            case BCOMMAND: this.commandCount++; break;
            case UNICODE: this.unicodeViolations++; break;
            case CAPS: this.capsViolations++; break;
            case SYNTAX: this.syntaxViolations++; break;
            case NONE: break;
        }
        this.globalViolations++;
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
            case NONE -> {}
        }
        this.globalViolations = this.spamCount + this.floodCount + this.regularCount + this.commandCount + this.unicodeViolations + this.capsViolations + this.syntaxViolations;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        Statistics stats = (Statistics)o;

        return this.globalViolations == stats.globalViolations;
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

    private Statistics(){
        this.spamCount = 0;
        this.capsViolations = 0;
        this.commandCount = 0;
        this.floodCount = 0;
        this.globalViolations = 0;
        this.regularCount = 0;
        this.unicodeViolations = 0;
        this.syntaxViolations = 0;
    }
}
