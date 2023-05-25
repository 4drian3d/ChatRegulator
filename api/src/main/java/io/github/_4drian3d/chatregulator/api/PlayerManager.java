package io.github._4drian3d.chatregulator.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * ChatRegulator's Player Manager
 */
public interface PlayerManager {
    /**
     * Obtain a player based on their UUID
     *
     * @param uuid the player UUID
     * @return a InfractionPlayer
     * @throws java.util.NoSuchElementException if the player with the provided UUID is not online or not cached
     */
    @NotNull InfractionPlayer getPlayer(final @NotNull UUID uuid);
}
