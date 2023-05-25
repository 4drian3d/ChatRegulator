package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Objects;

/**
 * Record of Infractions by a player
 */
public final class InfractionCount {
    private final EnumMap<InfractionType, Integer> infractionMap = new EnumMap<>(InfractionType.class);

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(final @NotNull InfractionType type) {
        if (type == InfractionType.GLOBAL) {
            throw new IllegalArgumentException("Invalid InfractionType provided");
        }
        infractionMap.merge(type, 1, Integer::sum);
    }

    /**
     * Sets the new number of infractions of some kind that the player will have.
     * @param type the type of infraction
     * @param newViolationsCount the new number of infractions
     */
    public void setViolations(final @NotNull InfractionType type, final int newViolationsCount) {
        if (type == InfractionType.GLOBAL) {
            throw new IllegalArgumentException("Invalid InfractionType provided");
        }
        infractionMap.put(type, newViolationsCount);
    }

    /**
     * Reset the count of infraction of any type of this player
     * @param types the types
     */
    public void resetViolations(final @NotNull InfractionType @NotNull... types) {
        for (final InfractionType type : types) {
            if (type == InfractionType.GLOBAL) {
                infractionMap.clear();
                return;
            }
            this.setViolations(type, 0);
        }
    }

    /**
     * Get the amount of violations of any type
     * @param type the violation type
     * @return the count
     */
    public @NonNegative int getCount(final @NotNull InfractionType type) {
        if (type == InfractionType.GLOBAL) {
            int count = 0;
            for (final int infraction : infractionMap.values()) {
                count += infraction;
            }
            return count;
        }
        return infractionMap.computeIfAbsent(type, $ -> 0);
    }

    @Override
    public boolean equals(final Object o){
        if (this==o) return true;
        if (!(o instanceof final InfractionCount that)) return false;
        return Objects.equals(this.infractionMap, that.infractionMap);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.infractionMap);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("ViolationCount[");
        infractionMap.forEach(((infractionType, integer) -> builder.append(infractionType).append('=').append(integer)));
        return builder.append("]").toString();
    }
}
