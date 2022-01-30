package me.dreamerzero.chatregulator.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Infraction Types
 */
public enum InfractionType {
    /**
     * Represents a regular violation, i.e.
     * detection based on the blacklist.yml file.
     */
    REGULAR(Permissions.BYPASS_INFRACTIONS),
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permissions.BYPASS_FLOOD),
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permissions.BYPASS_SPAM),
    /**
     * Represents a blocked command
     */
    BCOMMAND(Permissions.BYPASS_BCOMMAND),
    /**
     * Represents a Unicode check
     */
    UNICODE(Permissions.BYPASS_UNICODE),
    /**
     * Represents a Caps limit check
     */
    CAPS(Permissions.BYPASS_CAPS),
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE("chatregulator.no-permission");

    private final String bypassPermission;

    InfractionType(String permission){
        this.bypassPermission = permission;
    }

    /**
     * Get the bypass permission of this type
     * @return the bypass permission
     */
    public @NotNull String bypassPermission(){
        return this.bypassPermission;
    }
}
