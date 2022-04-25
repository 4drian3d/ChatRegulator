package me.dreamerzero.chatregulator.enums;

import java.util.function.Predicate;

import com.velocitypowered.api.command.CommandSource;

/**
 * ChatRegulator Permissions
 */
public final class Permission implements Predicate<CommandSource> {
    /**General Command Permission */
    public static final Permission COMMAND = of("chatregulator.command");

    /**Help Subcommand Permission */
    public static final Permission COMMAND_HELP = of("chatregulator.command.help");
    /**Stats Subcommand Permission */
    public static final Permission COMMAND_STATS = of("chatregulator.command.stats");
    /**Player Subcommand Permission */
    public static final Permission COMMAND_PLAYER = of("chatregulator.command.player");
    /**Reload Subcommand Permission */
    public static final Permission COMMAND_RELOAD = of("chatregulator.command.reload");

    /**Clear Subcommand Permission */
    public static final Permission COMMAND_CLEAR = of("chatregulator.command.clear");
    /**Server ChatClear Subcommand Permission */
    public static final Permission COMMAND_CLEAR_SERVER = of("chatregulator.command.clear.server");
    /**Player ChatClear Subcommand Permission */
    public static final Permission COMMAND_CLEAR_PLAYER = of("chatregulator.command.clear.player");

    /**Reset Subcommand Permission */
    public static final Permission COMMAND_RESET = of("chatregulator.command.reset");
    /**Regular Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_REGULAR = of("chatregulator.command.reset.regular");
    /**Flood Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_FLOOD = of("chatregulator.command.reset.flood");
    /**Spam Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_SPAM = of("chatregulator.command.reset.spam");
    /**Command Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_BCOMMAND = of("chatregulator.command.reset.command");
    /**Unicode Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_UNICODE = of("chatregulator.command.reset.unicode");
    /**Caps Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_CAPS = of("chatregulator.command.reset.caps");
    /**Syntax Reset Subcommand Permission */
    public static final Permission COMMAND_RESET_SYNTAX = of("chatregulator.command.reset.syntax");


    /**Notifications Permission */
    public static final Permission NOTIFICATIONS = of("chatregulator.notifications");
    /**CommandSpy alert */
    public static final Permission COMMANDSPY_ALERT = of("chatregulator.notifications.commandspy");


    /**Infractions Check Bypass */
    public static final Permission BYPASS_INFRACTIONS = of("chatregulator.bypass.infractions");
    /**Flood Check Bypass */
    public static final Permission BYPASS_FLOOD = of("chatregulator.bypass.flood");
    /**Spam Check Bypass */
    public static final Permission BYPASS_SPAM = of("chatregulator.bypass.spam");
    /**Blocked Commands Bypass */
    public static final Permission BYPASS_BCOMMAND = of("chatregulator.bypass.command");
    /**Unicode Check Bypass */
    public static final Permission BYPASS_UNICODE = of("chatregulator.bypass.unicode");
    /**Caps Check Bypass */
    public static final Permission BYPASS_CAPS = of("chatregulator.bypass.caps");
    /**Command Spy Bypass */
    public static final Permission BYPASS_COMMANDSPY = of("chatregulator.bypass.commandspy");
    /**Syntax Check Bypass */
    public static final Permission BYPASS_SYNTAX = of("chatregulator.bypass.syntax");

    public static final Permission NO_PERMISSION = of("chatregulator.no-permission");

    private final String permission;

    private Permission(final String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return this.permission;
    }

    @Override
    public boolean test(final CommandSource source) {
        return source.hasPermission(permission);
    }

    private static Permission of(String permission) {
        return new Permission(permission);
    }
}
