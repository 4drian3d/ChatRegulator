package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.enums.SourceType;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

/**
 * A player to whom the necessary warnings and variables can
 * be assigned in order to be sanctioned correctly.
 */
public interface InfractionPlayer extends ForwardingAudience.Single {
    /**
     * A simple method to obtain the player's name
     * @return infraction player name
     */
    @NotNull String username();

    /**
     * Returns the online status of the player
     * @return player online status
     */
    boolean isOnline();

    @NotNull StringChain getChain(final @NotNull SourceType sourceType);

    /**
     * Get the infractions count of the player
     * @return the infractions count
     * @since 2.0.0
     */
    @NotNull InfractionCount getInfractions();
}
