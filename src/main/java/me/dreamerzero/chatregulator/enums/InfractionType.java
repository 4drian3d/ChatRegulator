package me.dreamerzero.chatregulator.enums;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.config.Messages;

/**
 * Infraction Types
 */
public enum InfractionType {
    /**
     * Represents a regular violation, i.e.
     * detection based on the blacklist.yml file.
     */
    REGULAR(Permissions.BYPASS_INFRACTIONS, Configuration.getConfig().getInfractionsConfig(), Configuration.getMessages().getInfractionsMessages()),
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permissions.BYPASS_FLOOD, Configuration.getConfig().getFloodConfig(), Configuration.getMessages().getFloodMessages()),
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permissions.BYPASS_SPAM, Configuration.getConfig().getSpamConfig(), Configuration.getMessages().getSpamMessages()),
    /**
     * Represents a blocked command
     */
    BCOMMAND(Permissions.BYPASS_BCOMMAND, Configuration.getConfig().getCommandBlacklistConfig(), Configuration.getMessages().getBlacklistMessages()),
    /**
     * Represents a Unicode check
     */
    UNICODE(Permissions.BYPASS_UNICODE, Configuration.getConfig().getUnicodeConfig(), Configuration.getMessages().getUnicodeMessages()),
    /**
     * Represents a Caps limit check
     */
    CAPS(Permissions.BYPASS_CAPS, Configuration.getConfig().getCapsConfig(), Configuration.getMessages().getCapsMessages()),
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE;

    private String bypassPermission;
    private MainConfig.Toggleable config;
    private Messages.Warning messages;

    InfractionType(String permission, MainConfig.Toggleable config, Messages.Warning messages){
        this.bypassPermission = permission;
        this.config = config;
        this.messages = messages;
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

    public Messages.Warning getMessages(){
        return this.messages;
    }
}
