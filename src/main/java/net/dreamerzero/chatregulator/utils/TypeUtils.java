package net.dreamerzero.chatregulator.utils;

import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;

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
        REGULAR("chatregulator.bypass.infractions", Configuration.getConfig().getInfractionsConfig()),
        /**
         * Represents an infraction for repeating
         * the same character several times in a row.
         */
        FLOOD("chatregulator.bypass.flood", Configuration.getConfig().getFloodConfig()),
        /**
         * Represents an infraction for repeating
         * the same word or command several times.
         */
        SPAM("chatregulator.bypass.spam", Configuration.getConfig().getSpamConfig()),
        /**
         * Represents a blocked command
         */
        BCOMMAND("chatregulator.bypass.command", Configuration.getConfig().getCommandBlacklistConfig()),
        /**
         * Represents a Unicode check
         */
        UNICODE("chatregulator.bypass.unicode", Configuration.getConfig().getUnicodeConfig()),
        /**
         * Represents a Caps limit check
         */
        CAPS("chatregulator.bypass.caps", Configuration.getConfig().getCapsConfig()),
        /**
         * Used internally to represent a
         * multiple warning and in other cases more
         */
        NONE;

        private String bypassPermission;
        private MainConfig.Toggleable config;

        InfractionType(String permission, MainConfig.Toggleable config){
            this.bypassPermission = permission;
            this.config = config;
        }

        InfractionType(){
            this.bypassPermission = "";
        }

        public String bypassPermission(){
            return this.bypassPermission;
        }

        public MainConfig.Toggleable getConfig(){
            return this.config;
        }
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
