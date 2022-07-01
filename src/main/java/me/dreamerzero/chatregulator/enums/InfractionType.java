package me.dreamerzero.chatregulator.enums;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.config.MainConfig.Toggleable;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.config.Messages.Warning;

/**
 * Infraction Types
 */
public enum InfractionType {
    /**
     * Represents a regular violation, i.e.
     * detection based on the blacklist.yml file.
     */
    REGULAR(Permission.BYPASS_INFRACTIONS) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getInfractionsConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getInfractionsMessages();
        }
    },
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permission.BYPASS_FLOOD) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getFloodConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getFloodMessages();
        }
    },
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permission.BYPASS_SPAM) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getSpamConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getSpamMessages();
        }
    },
    /**
     * Represents a blocked command
     */
    BCOMMAND(Permission.BYPASS_BCOMMAND) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getCommandBlacklistConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getBlacklistMessages();
        }
    },
    /**
     * Represents a Unicode check
     */
    UNICODE(Permission.BYPASS_UNICODE) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getUnicodeConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getUnicodeMessages();
        }
    },
    /**
     * Represents a Caps limit check
     */
    CAPS(Permission.BYPASS_CAPS) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getCapsConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getCapsMessages();
        }
    },
    /**
     * Represents a Syntax check
     * <p>/minecraft:tp
     */
    SYNTAX(Permission.BYPASS_SYNTAX) {
        @Override
        public Toggleable getConfig() {
            return Configuration.getConfig().getSyntaxConfig();
        }

        @Override
        public Warning getMessages() {
            return Configuration.getMessages().getSyntaxMessages();
        }
    },
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE(Permission.NO_PERMISSION) {
        @Override
        public Toggleable getConfig() {
            return null;
        }

        @Override
        public Warning getMessages() {
            return null;
        }
    };

    private final Permission bypassPermission;

    InfractionType(Permission permission){
        this.bypassPermission = permission;
    }

    public abstract MainConfig.Toggleable getConfig();

    public abstract Messages.Warning getMessages();

    /**
     * Get the bypass permission of this type
     * @return the bypass permission
     */
    public @NotNull Permission bypassPermission(){
        return this.bypassPermission;
    }
}
