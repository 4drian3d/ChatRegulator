package me.dreamerzero.chatregulator.enums;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Configuration.Toggleable;
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
        public Toggleable getConfig(Configuration config) {
            return config.getInfractionsConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getInfractionsMessages();
        }
    },
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permission.BYPASS_FLOOD) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getFloodConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getFloodMessages();
        }
    },
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permission.BYPASS_SPAM) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getSpamConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getSpamMessages();
        }
    },
    /**
     * Represents a blocked command
     */
    BCOMMAND(Permission.BYPASS_BCOMMAND) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getCommandBlacklistConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getBlacklistMessages();
        }
    },
    /**
     * Represents a Unicode check
     */
    UNICODE(Permission.BYPASS_UNICODE) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getUnicodeConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getUnicodeMessages();
        }
    },
    /**
     * Represents a Caps limit check
     */
    CAPS(Permission.BYPASS_CAPS) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getCapsConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getCapsMessages();
        }
    },
    /**
     * Represents a Syntax check
     * <p>/minecraft:tp
     */
    SYNTAX(Permission.BYPASS_SYNTAX) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return config.getSyntaxConfig();
        }

        @Override
        public Warning getMessages(Messages messages) {
            return messages.getSyntaxMessages();
        }
    },
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE(Permission.NO_PERMISSION) {
        @Override
        public Toggleable getConfig(Configuration config) {
            return null;
        }

        @Override
        public Warning getMessages(Messages messages) {
            return null;
        }
    };

    private final Permission bypassPermission;

    InfractionType(Permission permission){
        this.bypassPermission = permission;
    }

    public abstract Configuration.Toggleable getConfig(Configuration config);

    public abstract Messages.Warning getMessages(Messages messages);

    /**
     * Get the bypass permission of this type
     * @return the bypass permission
     */
    public @NotNull Permission bypassPermission(){
        return this.bypassPermission;
    }
}
