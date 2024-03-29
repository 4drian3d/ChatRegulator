package io.github._4drian3d.chatregulator.api.enums;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.permission.PermissionChecker;

import java.util.function.Predicate;

/**
 * ChatRegulator Permissions
 */
public enum Permission implements Predicate<Audience> {
    /**General Command */
    COMMAND("chatregulator.command"),

    /**Stats Subcommand */
    COMMAND_STATS("chatregulator.command.stats"),
    /**Player Subcommand */
    COMMAND_PLAYER("chatregulator.command.player"),
    /**Reload Subcommand */
    COMMAND_RELOAD("chatregulator.command.reload"),

    /**Clear Subcommand */
    COMMAND_CLEAR("chatregulator.command.clear"),
    /**Server ChatClear Subcommand */
    COMMAND_CLEAR_SERVER("chatregulator.command.clear.server"),
    /**Player ChatClear Subcommand */
    COMMAND_CLEAR_PLAYER("chatregulator.command.clear.player"),

    /**Reset Subcommand */
    COMMAND_RESET("chatregulator.command.reset"),
    /**Regular Reset Subcommand */
    COMMAND_RESET_REGEX("chatregulator.command.reset.regex"),
    /**Flood Reset Subcommand */
    COMMAND_RESET_FLOOD("chatregulator.command.reset.flood"),
    /**Spam Reset Subcommand */
    COMMAND_RESET_SPAM("chatregulator.command.reset.spam"),
    /**Command Reset Subcommand */
    COMMAND_RESET_BLOCKEDCOMMAND("chatregulator.command.reset.command"),
    /**Unicode Reset Subcommand */
    COMMAND_RESET_UNICODE("chatregulator.command.reset.unicode"),
    /**Caps Reset Subcommand */
    COMMAND_RESET_CAPS("chatregulator.command.reset.caps"),
    /**Syntax Reset Subcommand */
    COMMAND_RESET_SYNTAX("chatregulator.command.reset.syntax"),


    /**Notifications */
    NOTIFICATIONS("chatregulator.notifications"),
    /**CommandSpy alert */
    COMMAND_SPY_ALERT("chatregulator.notifications.commandspy"),


    /**Infractions Check Bypass */
    BYPASS_REGEX("chatregulator.bypass.infractions"),
    /**Flood Check Bypass */
    BYPASS_FLOOD("chatregulator.bypass.flood"),
    /**Spam Check Bypass */
    BYPASS_SPAM("chatregulator.bypass.spam"),
    BYPASS_COOLDOWN("chatregulator.bypass.cooldown"),
    /**Blocked Commands Bypass */
    BYPASS_BLOCKED_COMMAND("chatregulator.bypass.command"),
    /**Unicode Check Bypass */
    BYPASS_UNICODE("chatregulator.bypass.unicode"),
    /**Caps Check Bypass */
    BYPASS_CAPS("chatregulator.bypass.caps"),
    /**Command Spy Bypass */
    BYPASS_COMMAND_SPY("chatregulator.bypass.commandspy"),
    /**Syntax Check Bypass */
    BYPASS_SYNTAX("chatregulator.bypass.syntax"),
    /**
     * No permission
     */
    NO_PERMISSION("chatregulator.no-permission");

    private final String permission;

    Permission(final String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return this.permission;
    }

    @Override
    public boolean test(final Audience source) {
        return source.get(PermissionChecker.POINTER)
                .filter(checker -> checker.test(this.permission))
                .isPresent();
    }
}
