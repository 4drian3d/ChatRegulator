package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

/**
 * Global Plugin Statistics
 */
public interface Statistics {
    /**
     * Obtain the number of infractions of some type
     *
     * @param type the infraction type
     * @return count of the respective infraction type
     */
    @NonNegative int getInfractionCount(final @NotNull InfractionType type);
}
