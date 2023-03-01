package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class InfractionCount {
    private int floodViolations;
    private int regularViolations;
    private int spamViolations;
    private int commandViolations;
    private int unicodeViolations;
    private int capsviolations;
    private int syntaxviolations;

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(@NotNull InfractionType type){
        switch (type) {
            case SPAM -> ++this.spamViolations;
            case REGULAR -> ++this.regularViolations;
            case FLOOD -> ++this.floodViolations;
            case BCOMMAND -> ++this.commandViolations;
            case UNICODE -> ++this.unicodeViolations;
            case CAPS -> ++this.capsviolations;
            case SYNTAX -> ++this.syntaxviolations;
        }
    }

    /**
     * Sets the new number of infractions of some kind that the player will have.
     * @param type the type of infraction
     * @param newViolationsCount the new number of infractions
     */
    public void setViolations(@NotNull InfractionType type, int newViolationsCount){
        switch (type) {
            case SPAM -> this.spamViolations = newViolationsCount;
            case REGULAR -> this.regularViolations = newViolationsCount;
            case FLOOD -> this.floodViolations = newViolationsCount;
            case BCOMMAND -> this.commandViolations = newViolationsCount;
            case UNICODE -> this.unicodeViolations = newViolationsCount;
            case CAPS -> this.capsviolations = newViolationsCount;
            case SYNTAX -> this.syntaxviolations = newViolationsCount;
        }
    }

    /**
     * Reset the count of infraction of any type of this player
     * @param types the types
     */
    public void resetViolations(@NotNull InfractionType @NotNull... types){
        for (InfractionType type : types){
            this.setViolations(type, 0);
        }
    }

    /**
     * Get the ammount of violations of any type
     * @param type the violation type
     * @return the count
     */
    public int getCount(@NotNull InfractionType type){
        return switch(type){
            case SPAM -> this.spamViolations;
            case REGULAR -> this.regularViolations;
            case FLOOD -> this.floodViolations;
            case BCOMMAND -> this.commandViolations;
            case UNICODE -> this.unicodeViolations;
            case CAPS -> this.capsviolations;
            case SYNTAX -> this.syntaxviolations;
            case NONE -> 0;
        };
    }

    @Override
    public boolean equals(Object o){
        if (this==o) return true;
        if (!(o instanceof final InfractionCount that)) return false;
        return this.spamViolations == that.spamViolations
                && this.capsviolations == that.capsviolations
                && this.commandViolations == that.commandViolations
                && this.floodViolations == that.floodViolations
                && this.regularViolations == that.regularViolations
                && this.unicodeViolations == that.unicodeViolations
                && this.syntaxviolations == that.syntaxviolations;
    }

    @Override
    public int hashCode(){
        return Objects.hash(
                this.spamViolations,
                this.regularViolations,
                this.capsviolations,
                this.commandViolations,
                this.unicodeViolations,
                this.floodViolations,
                this.syntaxviolations
        );
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
                +",syntax="+this.syntaxviolations
                +"]";
    }
}
