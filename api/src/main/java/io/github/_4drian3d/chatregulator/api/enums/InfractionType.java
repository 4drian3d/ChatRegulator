package io.github._4drian3d.chatregulator.api.enums;

public enum InfractionType {
    REGULAR(Permission.BYPASS_INFRACTIONS),
    /**
     * Represents an infraction for repeating
     * the same character several times in a row.
     */
    FLOOD(Permission.BYPASS_FLOOD),
    /**
     * Represents an infraction for repeating
     * the same word or command several times.
     */
    SPAM(Permission.BYPASS_SPAM),
    COOLDOWN(Permission.BYPASS_UNICODE),
    /**
     * Represents a blocked command
     */
    BLOCKED_COMMAND(Permission.BYPASS_BLOCKEDCOMMAND),
    /**
     * Represents a Unicode check
     */
    UNICODE(Permission.BYPASS_UNICODE),
    /**
     * Represents a Caps limit check
     */
    CAPS(Permission.BYPASS_CAPS),
    /**
     * Represents a Syntax check
     * <p>/minecraft:tp
     */
    SYNTAX(Permission.BYPASS_SYNTAX),
    /**
     * Used internally to represent a
     * multiple warning and in other cases more
     */
    NONE(Permission.NO_PERMISSION);

    private final Permission bypassPermission;

    InfractionType(Permission permission){
        this.bypassPermission = permission;
    }

    public Permission getBypassPermission() {
        return this.bypassPermission;
    }
}
