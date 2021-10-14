package net.dreamerzero.chatregulator.utils;

import java.util.List;

import de.leonhard.storage.Yaml;

/**
 * Utilities for the distinction of the detections performed
 */
public class TypeUtils {
    private Yaml config;
    /**
     * Creates an object to distinguish any detection
     * @param config the plugin config
     */
    public TypeUtils(Yaml config){
        this.config = config;
    }
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

    /**
     * Check if the command provided is within the list of commands to be checked.
     * @param command the command executed
     * @return if the command is to be checked
     */
    public boolean isCommand(String command){
        List<String> commandsChecked = config.getStringList("commands-checked");

        return commandsChecked.stream().anyMatch(command::contains);
    }
}
