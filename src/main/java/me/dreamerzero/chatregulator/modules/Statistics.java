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
        switch(type){
            case SPAM: return this.spamCount;
            case FLOOD: return this.floodCount;
            case REGULAR: return this.regularCount;
            case BCOMMAND: return this.commandCount;
            case UNICODE: return this.unicodeViolations;
            case CAPS: return this.capsViolations;
            case NONE: return this.globalViolations;
        }
        return 0;
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
    }
}
