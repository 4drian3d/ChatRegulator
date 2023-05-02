package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Objects;

public final class InfractionCount {
    private final EnumMap<InfractionType, Integer> infractionMap = new EnumMap<>(InfractionType.class);

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(@NotNull InfractionType type){
        infractionMap.merge(type, 1, Integer::sum);
        if (type != InfractionType.GLOBAL) {
            infractionMap.merge(InfractionType.GLOBAL, 1, Integer::sum);
        }
    }

    /**
     * Sets the new number of infractions of some kind that the player will have.
     * @param type the type of infraction
     * @param newViolationsCount the new number of infractions
     */
    public void setViolations(@NotNull InfractionType type, int newViolationsCount){
        infractionMap.put(type, newViolationsCount);
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
     * Get the amount of violations of any type
     * @param type the violation type
     * @return the count
     */
    public int getCount(@NotNull InfractionType type){
        return infractionMap.get(type);
    }

    @Override
    public boolean equals(Object o){
        if (this==o) return true;
        if (!(o instanceof final InfractionCount that)) return false;
        return Objects.equals(this.infractionMap, that.infractionMap);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.infractionMap);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder("ViolationCount[");
        infractionMap.forEach(((infractionType, integer) -> builder.append(infractionType).append('=').append(integer)));
        return builder.append("]").toString();
    }
}
