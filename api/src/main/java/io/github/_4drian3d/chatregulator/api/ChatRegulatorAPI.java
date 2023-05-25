package io.github._4drian3d.chatregulator.api;

import org.jetbrains.annotations.NotNull;

/**
 * Plugin API
 */
public interface ChatRegulatorAPI {
    /**
     * The PlayerManager has information about the connected players,
     * as well as their infractions and other data
     *
     * @return ChatRegulator's PlayerManager
     */
    @NotNull PlayerManager getPlayerManager();

    /**
     * Obtain the global statistics of infractions
     *
     * @return the global statistics
     */
    @NotNull Statistics getStatistics();
}