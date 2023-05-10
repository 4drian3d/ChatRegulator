package io.github._4drian3d.chatregulator.plugin.config;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.api.enums.*;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * Configuration values
 */
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public class Configuration implements Section {
    public static final String HEADER = """
        ChatRegulator | by 4drian3d
        Check the function of each configuration option at
        https://github.com/4drian3d/ChatRegulator/wiki/Configuration""";

    /**Main Configuration */
    @Comment("Regular regex module")
    private Regex regex = new Regex();

    @Comment("Flood Module")
    private Flood flood = new Flood();

    @Comment("Spam Module")
    private Spam spam = new Spam();

    @Comment("Cooldown Module")
    private Cooldown cooldown = new Cooldown();

    @Comment("Command blacklist module")
    @Setting(value = "command-blacklist")
    private CommandBlacklist blacklist = new CommandBlacklist();

    @Comment("Unicode Module")
    private Unicode unicode = new Unicode();

    @Comment("Caps Module")
    private Caps caps = new Caps();

    @Comment("Format Module")
    private Format format = new Format();

    @Comment("General Configurations")
    private General general = new General();

    @Comment("CommandSpy configuration")
    private CommandSpy commandSpy = new CommandSpy();

    @Comment("Syntax blocker configuration")
    private Syntax syntax = new Syntax();

    @Comment("""
        Specify in which commands you want the violations to be detected
        I recommend you to put chat commands, for example: /tell""")
    @Setting(value = "commands-checked")
    private Set<String> commandsChecked = Set.of(
        "tell",
        "etell",
        "msg",
        "emsg",
        "chat",
        "global",
        "reply"
    );

    /**
     * Get the commands checked
     * @return the commands checked
     */
    public Set<String> getCommandsChecked(){
        return this.commandsChecked;
    }

    /**
     * Get the command blacklist configuration
     * @return the command blacklist configuration
     */
    public CommandBlacklist getCommandBlacklistConfig(){
        return this.blacklist;
    }

    /**
     * Get the regex configuration
     * @return the command blacklist configuration
     */
    public Regex getRegexConfig(){
        return this.regex;
    }

    /**
     * Get the flood configuration
     * @return the flood configuration
     */
    public Flood getFloodConfig(){
        return this.flood;
    }

    /**
     * Get the spam configuration
     * @return the spam configuration
     */
    public Spam getSpamConfig(){
        return this.spam;
    }

    /**
     * Get the cooldown config
     * @return the cooldown config
     */
    public Cooldown getCooldownConfig(){
        return this.cooldown;
    }

    /**
     * Get the unicode configuration
     * @return the unicode configuration
     */
    public Unicode getUnicodeConfig(){
        return this.unicode;
    }

    /**
     * Get the caps configuration
     * @return the caps configuration
     */
    public Caps getCapsConfig(){
        return this.caps;
    }

    /**
     * Get the formats configuration
     * @return the formats configuration
     */
    public Format getFormatConfig(){
        return this.format;
    }

    /**
     * Get the general configuration
     * @return the general configuration
     */
    public General getGeneralConfig(){
        return this.general;
    }

    /**
     * Get the command spy configuration
     * @return the command spy configuration
     */
    public CommandSpy getCommandSpyConfig(){
        return this.commandSpy;
    }

    /**
     * Get the syntax blocker configuration
     * @return the syntax configuration
     */
    public Syntax getSyntaxConfig(){
        return this.syntax;
    }

    /**CommandBlacklist configuration */
    @ConfigSerializable
    public static class CommandBlacklist implements Executable, Warning, Toggleable {
        @Comment("Enables command blocking")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Commands to be executed in the command blacklist module")
        private CommandBlacklist.Commands commands = new Commands();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        @Override
        public CommandBlacklist.Commands getCommandsConfig(){
            return this.commands;
        }

        /**Command Blacklist commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    /**Regex configuration */
    @ConfigSerializable
    public static class Regex implements Toggleable, Warning, Controllable, Executable {
        @Comment("Enable violation checking in chat and commands")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("""
            Sets the control format
            Available options: BLOCK, REPLACE
            Note that in the latest versions of Velocity, the REPLACE mode may NOT work""")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("Commands to be executed in the regular regex module")
        private Regex.Commands commands = new Regex.Commands();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        @Override
        public ControlType getControlType(){
            return this.controlType;
        }

        @Override
        public CommandsConfig getCommandsConfig(){
            return this.commands;
        }

        /**Infraction commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    /**Flood Configuration */
    @ConfigSerializable
    public static class Flood implements Toggleable, Warning, Controllable, Executable {
        @Comment("""
            Enable flood check in the chat
            (e.g.: "aaaaaaaa")""")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("""
            Sets the control format
            Available options: BLOCK, REPLACE
            Note that in the latest versions of Velocity, the REPLACE mode may NOT work""")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("Sets the maximum limit of repeated characters for a word not to be considered as Flood")
        private int limit = 5;

        @Comment("Commands to be executed in the flood module")
        private Flood.Commands commands = new Flood.Commands();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        @Override
        public ControlType getControlType(){
            return this.controlType;
        }

        /**
         * Get the flood limit
         * @return the flood limit
         */
        public int getLimit(){
            return this.limit;
        }

        @Override
        public Flood.Commands getCommandsConfig(){
            return this.commands;
        }

        /**Flood Commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    /**Spam Configuration */
    @ConfigSerializable
    public static class Spam implements Toggleable, Warning, Executable {

        @Comment("Enable the spam module")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("The number of similar commands or chats messages sent by a player to consider as spam")
        private int similarStringCount = 5;

        @Comment("Commands to be executed in the cooldown module")
        private Spam.Commands commands = new Spam.Commands();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        public int getSimilarStringCount() {
            return similarStringCount;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        @Override
        public Spam.Commands getCommandsConfig(){
            return this.commands;
        }


        /**Spam Commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

    /**Cooldown cooldown configuration */
    @ConfigSerializable
    public static class Cooldown implements Toggleable, Warning, Executable {
        @Comment("Enables the cooldown submodule")
        private boolean enabled = true;

        @Comment("Set the time limit between each message")
        private long limit = 2500;

        @Comment("""
                Time Unit of the cooldown limit
                Available values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS""")
        private TimeUnit unit = TimeUnit.MILLISECONDS;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Commands to be executed in the cooldown module")
        private Cooldown.Commands commands = new Cooldown.Commands();

        public boolean enabled(){
            return this.enabled;
        }

        public long limit(){
            return this.limit;
        }

        public TimeUnit unit() {
            return this.unit;
        }

        @Override
        public WarningType getWarningType() {
            return this.warningType;
        }

        @Override
        public CommandsConfig getCommandsConfig() {
            return this.commands;
        }

        /**Cooldown Commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

    /**Unicode Configuration */
    @ConfigSerializable
    public static class Unicode implements Toggleable, Warning, Executable, Controllable {
        @Comment("Enable the Unicode Module")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("""
            Sets the control format
            Available options: BLOCK, REPLACE
            Note that in the latest versions of Velocity, the REPLACE mode may NOT work""")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("Commands to be executed in the unicode module")
        private Unicode.Commands commands = new Unicode.Commands();

        @Comment("Additional Characters to allow")
        private Chars additionalChars = new Chars();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        @Override
        public CommandsConfig getCommandsConfig(){
            return this.commands;
        }

        @Override
        public ControlType getControlType() {
            return controlType;
        }

        public Chars additionalChars() {
            return this.additionalChars;
        }

        /**Unicode Commands configuration */
        @ConfigSerializable
        public static class Commands extends CommandsConfig{}

        @ConfigSerializable
        public static class Chars implements Toggleable {
            @Comment("Enables extra character check")
            private boolean enabled = false;
            @Comment("Sets the additional characters to check")
            private char[] chars = {'ç'};
            @Comment("""
                Sets character checking mode
                Modes Availables:
                BLACKLIST: If one of the configured characters is detected, the check will be activated as an illegal character
                WHITELIST: If a character is detected as illegal but is within the configured characters, its detection as an illegal character will be skipped""")
            private DetectionMode mode = DetectionMode.BLACKLIST;

            public char[] chars() {
                return this.chars;
            }

            @Override
            public boolean enabled() {
                return this.enabled;
            }

            public DetectionMode detectionMode() {
                return this.mode;
            }

        }
    }

    /**Caps Configuration */
    @ConfigSerializable
    public static class Caps implements Warning, Toggleable, Controllable, Executable{

        @Comment("Enable the Caps limit Module")
        private boolean enabled = true;

        @Comment("""
            Sets the control format
            Available options: BLOCK, REPLACE
            Note that in the latest versions of Velocity, the REPLACE mode may NOT work""")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Sets the maximum limit of caps in a sentence")
        private int limit = 5;

        @Comment("Commands to be executed in the caps module")
        private Caps.Commands commands = new Caps.Commands();

        @Override
        public boolean enabled() {
            return this.enabled;
        }

        @Override
        public WarningType getWarningType() {
            return this.warningType;
        }

        @Override
        public ControlType getControlType(){
            return this.controlType;
        }

        public int limit(){
            return this.limit;
        }

        public CommandsConfig getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Syntax implements Warning, Toggleable, Executable {

        @Comment("Commands to be executed in the syntax module")
        private Syntax.Commands commands = new Syntax.Commands();

        @Comment("Enable the Syntax blocker Module")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        private Set<String> allowedCommands = Set.of();

        @Override
        public CommandsConfig getCommandsConfig() {
            return this.commands;
        }

        @Override
        public boolean enabled() {
            return this.enabled;
        }

        @Override
        public WarningType getWarningType() {
            return this.warningType;
        }

        public Set<String> getAllowedCommands() {
            return this.allowedCommands;
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig{}

    }

    @ConfigSerializable
    public static class Format implements Toggleable{
        @Comment("Enable Format Module")
        private boolean enabled = false;

        @Comment("Set the first letter of a sentence in uppercase")
        @Setting(value = "first-letter-uppercase")
        private boolean firstLetterUppercase = true;

        @Comment("Adds a final dot in each sentence")
        @Setting(value = "final-dot")
        private boolean finalDot = true;

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        public boolean setFirstLetterUppercase(){
            return this.firstLetterUppercase;
        }

        public boolean setFinalDot(){
            return this.finalDot;
        }
    }

    @ConfigSerializable
    public static class CommandSpy implements Toggleable{
        @Comment("Enable CommandSpy module")
        private boolean enabled = false;

        @Comment("Commands to ignore")
        private Set<String> ignoredCommands = Set.of(
            "login",
            "register",
            "changepassword"
        );

        @Override
        public boolean enabled() {
            return this.enabled;
        }

        public Set<String> ignoredCommands(){
            return this.ignoredCommands;
        }

        public boolean shouldAnnounce(CommandSource source, String command){
            return ignoredCommands().contains(Commands.getFirstArgument(command))
                    && Permission.BYPASS_COMMAND_SPY.test(source);
        }
    }

    @ConfigSerializable
    public static class General{
        @Comment("Set the maximum time in which a user's violations will be saved after the user leaves your server")
        @Setting(value = "delete-users-after")
        private long deleteUsersAfter = 30;

        @Comment("""
            Set the time unit of the delete-users-after setting
            Available values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS""")
        @Setting(value = "time-unit")
        private TimeUnit unit = TimeUnit.SECONDS;

        @Comment("Limit the ammount of users showed on autocompletion")
        @Setting(value = "tab-complete-limit")
        private int limitTabComplete = 40;

        public long deleteUsersTime(){
            return this.deleteUsersAfter;
        }

        public TimeUnit unit() {
            return this.unit;
        }

        public int tabCompleteLimit(){
            return this.limitTabComplete;
        }
    }

    @ConfigSerializable
    public static class CommandsConfig{
        @Comment("Enable submodule")
        @Setting(value = "execute-commands")
        private boolean executeCommands = false;

        @Comment("Violations required to execute the command")
        @Setting(value = "violations-required")
        private int violationsRequired = 2;

        @Comment("Commands to execute")
        @Setting(value = "commands-to-execute")
        private Set<String> commandsToExecute = Set.of(
                "mute <player> 1m You have been muted",
                "example command"
        );

        public boolean executeCommand(){
            return this.executeCommands;
        }

        public int violationsRequired(){
            return this.violationsRequired;
        }

        public Set<String> getCommandsToExecute(){
            return this.commandsToExecute;
        }
    }

    public interface Warning{
        WarningType getWarningType();
    }

    public interface Toggleable{
        boolean enabled();
    }

    public interface Controllable{
        ControlType getControlType();
    }

    public interface Executable{
        CommandsConfig getCommandsConfig();
    }

    public boolean isEnabled(InfractionType type) {
        return switch(type) {
            case REGEX -> regex.enabled();
            case FLOOD -> flood.enabled();
            case SPAM -> spam.enabled();
            case COOLDOWN -> cooldown.enabled();
            case BLOCKED_COMMAND ->  blacklist.enabled();
            case UNICODE -> unicode.enabled();
            case CAPS -> caps.enabled;
            case SYNTAX -> syntax.enabled();
            case GLOBAL -> true;
        };
    }

    private @Nullable Object getConfig(InfractionType type) {
        return switch (type) {
            case REGEX -> getRegexConfig();
            case FLOOD -> getFloodConfig();
            case SPAM -> getSpamConfig();
            case COOLDOWN -> getCooldownConfig();
            case BLOCKED_COMMAND -> getCommandBlacklistConfig();
            case UNICODE -> getUnicodeConfig();
            case CAPS -> getCapsConfig();
            case SYNTAX -> getSyntaxConfig();
            case GLOBAL -> null;
        };
    }

    public Warning getWarning(InfractionType type) {
        Object messages = getConfig(type);
        return messages == null
                ?  null : (Warning) messages;
    }

    public Executable getExecutable(InfractionType type) {
        Object messages = getConfig(type);
        return messages == null
                ?  null : (Executable) messages;
    }

}
