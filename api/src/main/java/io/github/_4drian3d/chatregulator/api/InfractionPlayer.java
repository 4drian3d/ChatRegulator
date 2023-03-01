package io.github._4drian3d.chatregulator.api;

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

    /**
     * Get the message prior to the player's last message
     * @return the message before the player's last message
     */
    @NotNull String preLastMessage();

    /**
     * Get the last message sent by the player
     * @return last message of the player
     */
    @NotNull String lastMessage();

    /**
     * Get the command prior to the player's last command
     * @return the command before the player's last command
     */
    @NotNull String preLastCommand();

    /**
     * Get the last command executed by the player
     * @return last command of the player
     */
    @NotNull String lastCommand();

    /**
     * Get the time in milliseconds since the last message of the player
     * @return time in milliseconds since the last message
     */
    long getTimeSinceLastMessage();

    /**
     * Get the time in milliseconds since the last command of the player
     * @return time in milliseconds since the last command
     */
    long getTimeSinceLastCommand();

    /**
     * Get the infractions count of the player
     * @return the infractions count
     * @since 2.0.0
     */
    @NotNull InfractionCount getInfractions();
}
