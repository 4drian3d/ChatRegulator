package me.dreamerzero.chatregulator.enums;

/**
 * ChatRegulator Permissions
 */
public final class Permissions {
    /**Command Permission */
    public static final String COMMAND = "chatregulator.command";
    /**Notifications Permission */
    public static final String NOTIFICATIONS = "chatregulator.notifications";
    /**CommandSpy alert */
    public static final String COMMANDSPY_ALERT = "chatregulator.notifications.commandspy";

    /**Infractions Check Bypass */
    public static final String BYPASS_INFRACTIONS = "chatregulator.bypass.infractions";
    /**Flood Check Bypass */
    public static final String BYPASS_FLOOD = "chatregulator.bypass.flood";
    /**Spam Check Bypass */
    public static final String BYPASS_SPAM = "chatregulator.bypass.spam";
    /**Blocked Commands Bypass */
    public static final String BYPASS_BCOMMAND = "chatregulator.bypass.command";
    /**Unicode Check Bypass */
    public static final String BYPASS_UNICODE = "chatregulator.bypass.unicode";
    /**Caps Check Bypass */
    public static final String BYPASS_CAPS = "chatregulator.bypass.caps";
    /**Command Spy Bypass */
    public static final String BYPASS_COMMANDSPY = "chatregulator.bypass.commandspy";
    /**Syntax Check Bypass */
    public static final String BYPASS_SYNTAX = "chatregulator.bypass.syntax";

    private Permissions(){}
}
