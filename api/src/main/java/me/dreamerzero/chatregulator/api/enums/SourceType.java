package me.dreamerzero.chatregulator.api.enums;

/**
 * The type of detection the infraction has been detected
 */
public enum SourceType {
    /**
     * Represents a detection performed
     * when the player has executed a command.
     */
    COMMAND,
    /**
     * Represents a detection made when the player
     * has written a comment in the chat.
     */
    CHAT;
}
