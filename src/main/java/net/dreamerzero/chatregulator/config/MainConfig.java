package net.dreamerzero.chatregulator.config;

import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import net.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import net.dreamerzero.chatregulator.utils.TypeUtils.WarningType;

public class MainConfig {
    private MainConfig(){}
    @ConfigSerializable
    public static class Config {
        @Comment("Command blacklist module")
        private CommandBlacklist blacklist = new CommandBlacklist();

        @Comment("Regular violation module")
        private Infractions infractions = new Infractions();

        @Comment("Flood Module")
        private Flood flood = new Flood();

        @Comment("Spam Module")
        private Spam spam = new Spam();

        @Comment("Unicode Module")
        private Unicode unicode = new Unicode();

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

        public Format getFormatConfig(){
            return this.format;
        }

        public General getGeneralConfig(){
            return this.general;
        }
    }

    @ConfigSerializable
    public static class CommandBlacklist{
        @Comment("Enables command blocking")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        private WarningType warning_type = WarningType.MESSAGE;

        @Comment("Commands to be executed in the command blacklist module")
        private CommandBlacklist.Commands commands = new Commands();

        public boolean enabled(){
            return this.enabled;
        }

        public WarningType getWarningType(){
            return this.warning_type;
        }

        public CommandBlacklist.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands{
            @Comment("Enable submodule")
            private boolean execute_commands = false;

            @Comment("Violations required to execute the command")
            private int violations_required = 2;

            @Comment("Commands to execute")
            private Set<String> commands_to_execute = Set.of(
                "mute <player> 1m You have been muted for executing blocked commands",
                "example command"
            );

            public boolean executeCommand(){
                return this.execute_commands;
            }

            public int violationsRequired(){
                return this.violations_required;
            }

            public Set<String> getCommandsToExecute(){
                return this.commands_to_execute;
            }
        }
    }

    @ConfigSerializable
    public static class Infractions{
        @Comment("Enable violation checking in chat and commands")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        private WarningType warning_type = WarningType.MESSAGE;

        @Comment("Sets the control format\nAvailable options: BLOCK, REPLACE")
        private ControlType control_type = ControlType.BLOCK;

        @Comment("Specify in which commands you want the violations to be detected\nI recommend you to put chat commands, for example: /tell")
        private Set<String> commands_checked = Set.of(
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

        public boolean enabled(){
            return this.enabled;
        }

        public WarningType getWarningType(){
            return this.warning_type;
        }

        public ControlType getControlType(){
            return this.control_type;
        }

        public Set<String> getCommandsChecked(){
            return this.commands_checked;
        }

        public Infractions.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands{
            @Comment("Enable submodule")
            private boolean execute_commands = false;

            @Comment("Violations required to execute the command")
            private int violations_required = 3;

            @Comment("Commands to execute")
            private Set<String> commands_to_execute = Set.of(
                "mute <player> 2m You have been muted for swearing on the server <server>",
                "examplee command"
            );

            public boolean executeCommand(){
                return this.execute_commands;
            }

            public int violationsRequired(){
                return this.violations_required;
            }

            public Set<String> getCommandsToExecute(){
                return this.commands_to_execute;
            }
        }
    }

    @ConfigSerializable
    public static class Flood{
        @Comment("Enable flood check in the chat\n(e.g.: \"aaaaaaaaaaaaaaaaaaaa\")")
        private boolean enabled = true;

        @Comment("Sets the form of warning\nAvailable options: TITLE, ACTIONBAR, MESSAGE")
        private WarningType warning_type = WarningType.MESSAGE;

        @Comment("Sets the control format\nAvailable options: BLOCK, REPLACE")
        private ControlType control_type = ControlType.BLOCK;

        @Comment("Sets the maximum limit of repeated characters for a word not to be considered as Flood")
        private int limit = 5;

        @Comment("Commands to be executed in the flood module")
        private Flood.Commands commands = new Flood.Commands();

        public boolean enabled(){
            return this.enabled;
        }

        public WarningType getWarningType(){
            return this.warning_type;
        }

        public ControlType getControlType(){
            return this.control_type;
        }

        public int getLimit(){
            return this.limit;
        }

        public Flood.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands{
            @Comment("Enable submodule")
            private boolean execute_commands = false;

            @Comment("Violations required to execute the command")
            private int violations_required = 4;

            @Comment("Commands to execute")
            private Set<String> commands_to_execute = Set.of(
                "mute <player> 1m You have been muted for flooding the chat on the server <server>",
                "examplee command"
            );

            public boolean executeCommand(){
                return this.execute_commands;
            }

            public int violationsRequired(){
                return this.violations_required;
            }

            public Set<String> getCommandsToExecute(){
                return this.commands_to_execute;
            }
        }
    }

    @ConfigSerializable
    public static class Spam{
        private boolean enabled = true;

        private WarningType warning_type = WarningType.MESSAGE;

        private Spam.Cooldown cooldown = new Spam.Cooldown();

        private Spam.Commands commands = new Spam.Commands();

        public boolean enabled(){
            return this.enabled;
        }

        public WarningType getWarningType(){
            return this.warning_type;
        }

        public Spam.Cooldown getCooldownConfig(){
            return this.cooldown;
        }

        public Spam.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Cooldown{
            
            private boolean enabled = true;

            
            private int limit = 2500;

            public boolean enabled(){
                return this.enabled;
            }

            public int limit(){
                return this.limit;
            }
        }

        @ConfigSerializable
        public static class Commands{
            @Comment("Enable submodule")
            private boolean execute_commands = false;

            @Comment("Violations required to execute the command")
            private int violations_required = 4;

            @Comment("Commands to execute")
            private Set<String> commands_to_execute = Set.of(
                "mute <player> 1m You have been muted for spam on the server <server>",
                "examplee spam command"
            );

            public boolean executeCommand(){
                return this.execute_commands;
            }

            public int violationsRequired(){
                return this.violations_required;
            }

            public Set<String> getCommandsToExecute(){
                return this.commands_to_execute;
            }
        }
    }

    @ConfigSerializable
    public static class Unicode{
        private boolean enabled = true;

        private WarningType warning_type = WarningType.MESSAGE;

        private Unicode.Commands commands = new Unicode.Commands();

        public boolean enabled(){
            return this.enabled;
        }

        public WarningType getWarningType(){
            return this.warning_type;
        }

        public Unicode.Commands getCommandsConfig(){
            return this.commands;
        }

        @ConfigSerializable
        public static class Commands{
            @Comment("Enable submodule")
            private boolean execute_commands = false;

            @Comment("Violations required to execute the command")
            private int violations_required = 2;

            @Comment("Commands to execute")
            private Set<String> commands_to_execute = Set.of(
                "mute <player> 1m You have been muted for use symbols on the server <server>",
                "examplee unicode command"
            );

            public boolean executeCommand(){
                return this.execute_commands;
            }

            public int violationsRequired(){
                return this.violations_required;
            }

            public Set<String> getCommandsToExecute(){
                return this.commands_to_execute;
            }
        }
    }

    @ConfigSerializable
    public static class Format{
        @Comment("Enable Format Module")
        private boolean enabled = false;

        @Comment("Set the first letter of a sentence in uppercase")
        private boolean first_letter_uppercase = true;

        @Comment("Adds a final dot in each sentence")
        private boolean final_dot = true;

        public boolean enabled(){
            return this.enabled;
        }

        public boolean setFirstLetterUppercase(){
            return this.first_letter_uppercase;
        }

        public boolean setFinalDot(){
            return this.final_dot;
        }
    }

    @ConfigSerializable
    public static class General{
        @Comment("Set the maximum time in which a user's violations will be saved after the user leaves your server")
        private long delete_users_after = 3000;

        @Comment("Limit the ammount of users showed on autocompletion")
        private int limit_tab_complete = 40;

        public long deleteUsersTime(){
            return this.delete_users_after;
        }

        public int tabCompleteLimit(){
            return this.limit_tab_complete;
        }
    }
}
