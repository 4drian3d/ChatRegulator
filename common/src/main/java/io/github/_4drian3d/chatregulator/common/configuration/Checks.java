package io.github._4drian3d.chatregulator.common.configuration;

import io.github._4drian3d.chatregulator.api.enums.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@ConfigSerializable
public final class Checks implements Section {
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

    @Comment("Syntax blocker configuration")
    private Syntax syntax = new Syntax();
    
    public CommandBlacklist getCommandBlacklistConfig(){
        return this.blacklist;
    }
    
    public Regex getRegexConfig(){
        return this.regex;
    }
    
    public Flood getFloodConfig(){
        return this.flood;
    }
    
    public Spam getSpamConfig(){
        return this.spam;
    }
    
    public Cooldown getCooldownConfig(){
        return this.cooldown;
    }
    
    public Unicode getUnicodeConfig(){
        return this.unicode;
    }
    
    public Caps getCapsConfig(){
        return this.caps;
    }
    
    public Syntax getSyntaxConfig(){
        return this.syntax;
    }

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

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

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
            Available options: BLOCK, REPLACE""")
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

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

    @ConfigSerializable
    public static class Flood implements Toggleable, Warning, Controllable, Executable {
        @Comment("Enable flood check in the chat")
        private boolean enabled = true;

        @Comment("""
            Sets the form of warning
            Available options: TITLE, ACTIONBAR, MESSAGE""")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("""
            Sets the control format
            Available options: BLOCK, REPLACE""")
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

        public int getLimit(){
            return this.limit;
        }

        @Override
        public Flood.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

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
        public int similarStringCount = 5;

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


        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

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

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
    }

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
            Available options: BLOCK, REPLACE""")
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

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}

        @ConfigSerializable
        public static class Chars implements Toggleable {
            @Comment("Enables extra character check")
            private boolean enabled = false;
            @Comment("Sets the additional characters to check")
            private char[] chars = {'ç'};
            @Comment("""
                Sets character checking mode
                Modes Available:
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

    @ConfigSerializable
    public static class Caps implements Warning, Toggleable, Controllable, Executable {

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

        @Comment("""
                Select caps detection algorithm
                AMOUNT
                |- In case the provided string has a specific amount of capital letters,
                   it will be detected
                PERCENTAGE
                |- In case the provided string has a higher percentage of capital letters than specified,
                   it will be detected""")
        private CapsAlgorithm algorithm = CapsAlgorithm.AMOUNT;

        @Comment("Sets the uppercase limit in a sentence according to the selected detection algorithm")
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

        public CapsAlgorithm getAlgorithm() {
            return algorithm;
        }

        public int limit(){
            return this.limit;
        }

        public CommandsConfig getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig {}
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

        @Comment("Allowed syntax command executions")
        private Set<String> allowedCommands = Set.of("luckperms");

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
        public static class Commands extends CommandsConfig {}

    }

    @ConfigSerializable
    public static class CommandsConfig {
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

    public interface Warning {
        WarningType getWarningType();
    }

    public interface Toggleable {
        boolean enabled();
    }

    public interface Controllable {
        ControlType getControlType();
    }

    public interface Executable {
        CommandsConfig getCommandsConfig();
    }

    public boolean isEnabled(final InfractionType type) {
        return switch(type) {
            case REGEX -> regex.enabled();
            case FLOOD -> flood.enabled();
            case SPAM -> spam.enabled();
            case COOLDOWN -> cooldown.enabled();
            case BLOCKED_COMMAND -> blacklist.enabled();
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
            case GLOBAL -> throw new IllegalArgumentException("Invalid InfractionType provided");
        };
    }

    public Warning getWarning(final InfractionType type) {
        return (Warning) getConfig(type);
    }

    public Executable getExecutable(final InfractionType type) {
        return (Executable) getConfig(type);
    }
}
