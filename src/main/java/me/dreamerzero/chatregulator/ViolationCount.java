package me.dreamerzero.chatregulator;

import me.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class ViolationCount {
    private int floodViolations;
    private int regularViolations;
    private int spamViolations;
    private int commandViolations;
    private int unicodeViolations;
    private int capsviolations;

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(InfractionType type){
        switch(type){
            case SPAM: this.spamViolations++; break;
            case REGULAR: this.regularViolations++; break;
            case FLOOD: this.floodViolations++; break;
            case BCOMMAND: this.commandViolations++; break;
            case UNICODE: this.unicodeViolations++; break;
            case CAPS: this.capsviolations++; break;
            case NONE: return;
        }
    }

    /**
     * Sets the new number of infractions of some kind that the player will have.
     * @param type the type of infraction
     * @param newViolationsCount the new number of infractions
     */
    public void setViolations(InfractionType type, int newViolationsCount){
        switch(type){
            case SPAM: this.spamViolations = newViolationsCount; break;
            case REGULAR: this.regularViolations = newViolationsCount; break;
            case FLOOD: this.floodViolations = newViolationsCount; break;
            case BCOMMAND: this.commandViolations = newViolationsCount; break;
            case UNICODE: this.unicodeViolations = newViolationsCount; break;
            case CAPS: this.capsviolations = newViolationsCount; break;
            case NONE: return;
        }
    }

    /**
     * Reset the count of infraction of any type of this player
     * @param types the types
     */
    public void resetViolations(InfractionType... types){
        for(InfractionType type : types){
            this.setViolations(type, 0);
        }
    }

    /**
     * Get the ammount of violations of any type
     * @param type the violation type
     * @return the count
     */
    public int getCount(InfractionType type){
        switch(type){
            case SPAM: return this.spamViolations;
            case REGULAR: return this.regularViolations;
            case FLOOD: return this.floodViolations;
            case BCOMMAND: return this.commandViolations;
            case UNICODE: return this.unicodeViolations;
            case CAPS: return this.capsviolations;
            case NONE: break;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof ViolationCount)) return false;
        ViolationCount other = (ViolationCount)o;
        return this.spamViolations == other.spamViolations
            && this.capsviolations == other.capsviolations
            && this.commandViolations == other.commandViolations
            && this.floodViolations == other.floodViolations
            && this.regularViolations == other.regularViolations
            && this.unicodeViolations == other.unicodeViolations;
    }

    @Override
    public int hashCode(){
        return 31
            + this.spamViolations
            + this.regularViolations
            + this.capsviolations
            + this.commandViolations
            + this.unicodeViolations
            + this.floodViolations;
    }

    @Override
    public String toString(){
        return "ViolationCount["
            +"regular="+this.regularViolations
            +",flood="+this.floodViolations
            +",spam="+this.spamViolations
            +",caps="+this.capsviolations
            +",command="+this.commandViolations
            +",unicode="+this.unicodeViolations
            +"]";
    }
}
