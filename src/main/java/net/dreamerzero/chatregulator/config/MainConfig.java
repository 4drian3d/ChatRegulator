package net.dreamerzero.chatregulator.config;

import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import net.dreamerzero.chatregulator.utils.TypeUtils.WarningType;

public class MainConfig {
    private MainConfig(){}
    @ConfigSerializable
    public static class Config {
        private CommandBlacklist blacklist = new CommandBlacklist();

        private Infractions infractions = new Infractions();

        private Flood flood = new Flood();

        private Spam spam = new Spam();

        private Unicode unicode = new Unicode();

        private Format format = new Format();

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
        private boolean enabled = true;

        private WarningType warning_type = WarningType.MESSAGE;

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
            private boolean execute_commands = false;

            private int violations_required = 2;

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
        private boolean enabled = true;

        private WarningType warning_type = WarningType.MESSAGE;

        private ControlType control_type = ControlType.BLOCK;

        private Set<String> commands_checked = Set.of(
            "tell",
            "etell",
            "msg",
            "emsg",
            "chat",
            "global",
            "reply"
        );

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
            private boolean execute_commands = false;

            private int violations_required = 3;

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
        private boolean enabled = true;

        private WarningType warning_type = WarningType.MESSAGE;

        private ControlType control_type = ControlType.BLOCK;

        private int limit = 5;

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
            private boolean execute_commands = false;

            private int violations_required = 4;

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
            private boolean execute_commands = false;

            private int violations_required = 4;

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
            private boolean execute_commands = false;

            private int violations_required = 2;

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
        private boolean enabled = false;

        private boolean first_letter_uppercase = true;

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
        private long delete_users_after = 3000;

        private int limit_tab_complete = 40;

        public long deleteUsersTime(){
            return this.delete_users_after;
        }

        public int tabCompleteLimit(){
            return this.limit_tab_complete;
        }
    }
}
