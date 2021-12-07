package net.dreamerzero.chatregulator.config;

import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import net.dreamerzero.chatregulator.utils.TypeUtils.WarningType;

public class MainConfig {
    private MainConfig(){}
    @ConfigSerializable
    public static class Config {
        @Comment("Regular infraction module")
        private Infractions infractions = new Infractions();

        @Comment("Flood Module")
        private Flood flood = new Flood();

        @Comment("Spam Module")
        private Spam spam = new Spam();

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

        public CommandBlacklist getCommandBlacklistConfig(){
            return this.blacklist;
        }

        public Infractions getInfractionsConfig(){
            return this.infractions;
        }

        public Flood getFloodConfig(){
            return this.flood;
        }

        public Spam getSpamConfig(){
            return this.spam;
        }

        public Unicode getUnicodeConfig(){
            return this.unicode;
        }

        public Caps getCapsConfig(){
            return this.caps;
        }

        public Format getFormatConfig(){
            return this.format;
        }

        public General getGeneralConfig(){
            return this.general;
        }
    }

    @ConfigSerializable
    public static class CommandBlacklist implements Executable, Warning, Toggleable {
        @Comment("Enables command blocking")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
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
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Infractions implements Toggleable, Warning, Controllable, Executable {
        @Comment("Enable violation checking in chat and commands")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Sets the control format\nAvailable options: BLOCK, REPLACE")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("Specify in which commands you want the violations to be detected\nI recommend you to put chat commands, for example: /tell")
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

        @Comment("Commands to be executed in the regular infraction module")
        private Infractions.Commands commands = new Infractions.Commands();

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

        public Set<String> getCommandsChecked(){
            return this.commandsChecked;
        }

        @Override
        public CommandsConfig getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Flood implements Toggleable, Warning, Controllable, Executable {
        @Comment("Enable flood check in the chat\n(e.g.: \"aaaaaaaaaaaaaaaaaaaa\")")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Sets the control format\nAvailable options: BLOCK, REPLACE")
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
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Spam implements Toggleable, Warning, Executable {

        @Comment("Enable the spam module")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Cooldown subcheck configuration")
        private Spam.Cooldown cooldown = new Spam.Cooldown();

        @Comment("Commands to be executed in the flood module")
        private Spam.Commands commands = new Spam.Commands();

        @Override
        public boolean enabled(){
            return this.enabled;
        }

        @Override
        public WarningType getWarningType(){
            return this.warningType;
        }

        public Spam.Cooldown getCooldownConfig(){
            return this.cooldown;
        }

        @Override
        public Spam.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Cooldown{
            @Comment("Enables the cooldown submodule")
            private boolean enabled = true;

            @Comment("Set the time limit between each message (in milliseconds)")
            private int limit = 2500;

            public boolean enabled(){
                return this.enabled;
            }

            public int limit(){
                return this.limit;
            }
        }

        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Unicode implements Toggleable, Warning, Executable {
        @Comment("Enable the Unicode Module")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        @Setting(value = "warning-type")
        private WarningType warningType = WarningType.MESSAGE;

        @Comment("Commands to be executed in the unicode module")
        private Unicode.Commands commands = new Unicode.Commands();

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

        @ConfigSerializable
        public static class Commands extends CommandsConfig{}
    }

    @ConfigSerializable
    public static class Caps implements Warning, Toggleable, Controllable, Executable{

        @Comment("Enable the Caps limit Module")
        private boolean enabled = true;

        @Comment("Sets the control format\nAvailable options: BLOCK, REPLACE")
        @Setting(value = "control-type")
        private ControlType controlType = ControlType.BLOCK;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
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
    public static class General{
        @Comment("Set the maximum time in which a user's violations will be saved after the user leaves your server")
        @Setting(value = "delete-users-after")
        private long deleteUsersAfter = 3000;

        @Comment("Limit the ammount of users showed on autocompletion")
        @Setting(value = "tab-complete-limit")
        private int limitTabComplete = 40;

        public long deleteUsersTime(){
            return this.deleteUsersAfter;
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
}
