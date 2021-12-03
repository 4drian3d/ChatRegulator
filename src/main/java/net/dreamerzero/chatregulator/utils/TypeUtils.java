package net.dreamerzero.chatregulator.utils;

/**
 * Utilities for the distinction of the detections performed
 */
public final class TypeUtils {
    /**
     * The warning format to be executed
     */
    public enum WarningType {
        /**
         * Title type format. Will send only a subtitle
         * if the ";" character is not supplied.
         */
        TITLE,
        /**
         * Actionbar type format.
         */
        ACTIONBAR,
        /**
         * Simple message format
         */
        MESSAGE
    }

    /**
     * The type of violation detected
     */
    public enum InfractionType{
        /**
         * Represents a regular violation, i.e.
         * detection based on the blacklist.yml file.
         */
        REGULAR,
        /**
         * Represents an infraction for repeating
         * the same character several times in a row.
         */
        FLOOD,
        /**
         * Represents an infraction for repeating
         * the same word or command several times.
         */
        SPAM,
        /**
         * Represents a blocked command
         */
        BCOMMAND,
        /**
         * Represents a Unicode check
         */
        UNICODE,
        /**
         * Represents a Caps limit check
         */
        CAPS,
        /**
         * Used internally to represent a
         * multiple warning and in other cases more
         */
        NONE
    }

    /**
     * The type of detection the infraction has been detected
     */
    public enum SourceType{
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

    public enum ControlType {
        /**
         * Block the entire message
         */
        BLOCK,
        /**
         * Replace the infraction
         */
        REPLACE
    }
}
