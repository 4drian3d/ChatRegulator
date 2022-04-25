package me.dreamerzero.chatregulator.enums;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

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
    REGULAR(Permission.BYPASS_INFRACTIONS, () -> Configuration.getConfig().getInfractionsConfig(), () -> Configuration.getMessages().getInfractionsMessages()),
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permission.BYPASS_FLOOD, () -> Configuration.getConfig().getFloodConfig(), () -> Configuration.getMessages().getFloodMessages()),
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permission.BYPASS_SPAM, () -> Configuration.getConfig().getSpamConfig(), () -> Configuration.getMessages().getSpamMessages()),
    /**
     * Represents a blocked command
     */
    BCOMMAND(Permission.BYPASS_BCOMMAND, () -> Configuration.getConfig().getCommandBlacklistConfig(), () -> Configuration.getMessages().getBlacklistMessages()),
    /**
     * Represents a Unicode check
     */
    UNICODE(Permission.BYPASS_UNICODE, () -> Configuration.getConfig().getUnicodeConfig(), () -> Configuration.getMessages().getUnicodeMessages()),
    /**
     * Represents a Caps limit check
     */
    CAPS(Permission.BYPASS_CAPS, () -> Configuration.getConfig().getCapsConfig(), () -> Configuration.getMessages().getCapsMessages()),
    /**
     * Represents a Syntax check
     * <p>/minecraft:tp
     */
    SYNTAX(Permission.BYPASS_SYNTAX, () -> Configuration.getConfig().getSyntaxConfig(), () -> Configuration.getMessages().getSyntaxMessages()),
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE(Permission.NO_PERMISSION);

    private final Permission bypassPermission;
    private Supplier<MainConfig.Toggleable> config;
    private Supplier<Messages.Warning> messages;

    InfractionType(Permission permission){
        this.bypassPermission = permission;
    }

    InfractionType(Permission permission, Supplier<MainConfig.Toggleable> config, Supplier<Messages.Warning> messages){
        this(permission);
        this.config = config;
        this.messages = messages;
    }

    public Supplier<MainConfig.Toggleable> getConfig(){
        return this.config;
    }

    public Supplier<Messages.Warning> getMessages(){
        return this.messages;
    }

    /**
     * Get the bypass permission of this type
     * @return the bypass permission
     */
    public @NotNull Permission bypassPermission(){
        return this.bypassPermission;
    }
}
