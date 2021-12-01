package net.dreamerzero.chatregulator.config;

import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public class Messages {
    private Messages(){}

    @ConfigSerializable
    public static class Config{
        @Comment("Configuration of command blacklist module messages")
        @Setting(value = "command-blacklist")
        private CommandBlacklist blacklist = new CommandBlacklist();

        @Comment("Configuration of regular violation module messages")
        private Infractions infractions = new Infractions();

        @Comment("Configuration of flood module messages")
        private Flood flood = new Flood();

        @Comment("Configuration of spam module messages")
        private Spam spam = new Spam();

        @Comment("Configuration of unicode module messages")
        private Unicode unicode = new Unicode();

        @Comment("Configuration of caps module messages")
        private Caps caps = new Caps();

        @Comment("Configuration of the messages of the /chatr clear command")
        @Setting(value = "clear-chat")
        private Clear clearChat = new Clear();

        @Comment("General Messages")
        private General general = new General();

        public Infractions getInfractionsMessages(){
            return this.infractions;
        }

        public CommandBlacklist getBlacklistMessages(){
            return this.blacklist;
        }

        public Flood getFloodMessages(){
            return this.flood;
        }

        public Spam getSpamMessages(){
            return this.spam;
        }

        public Unicode getUnicodeMessages(){
            return this.unicode;
        }

        public Caps getCapsMessages(){
            return this.caps;
        }

        public Clear getClearMessages(){
            return this.clearChat;
        }

        public General getGeneralMessages(){
            return this.general;
        }
    }

    @ConfigSerializable
    public static class CommandBlacklist{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to use blocked commands";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has executed blocked commands in <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The commands infraction count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Infractions{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to use dirty words on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The infraction warning count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Flood{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to make flood on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has make flood in <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The flood warning count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Spam{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to make spam on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The spam warning count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Unicode{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to use this symbols";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was using unicode symbols in the chat of <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The simbols sended count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Caps{
        @Comment("Message to be sent to the offender\nDepending on your warning-type section settings, it will be sent as Title, Actionbar or Message\nIn case you use the Title mode, put a ; to delimit the title and the subtitle")
        private String warning = "<red>Hello, it is not allowed to use this symbols";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was using many caps in the chat of <aqua><server></aqua> server";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The simbols sended count for <player> was reset";

        public String getWarningMessage(){
            return this.warning;
        }

        public String getAlertMessage(){
            return this.alert;
        }

        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Clear{
        @Comment("Confirmation of global chat cleanup")
        private String global = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat has been cleaned up";

        @Comment("Confirmation of chat cleanup on a server")
        private String server = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the server <white><server></white> has been cleared";

        @Comment("Message to send if a server was not found")
        @Setting(value = "server-not-fount")
        private String serverNotFound = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The <white><server></white> server was not found";

        @Comment("Confirmation of chat clearing for a user")
        private String player = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the player <white><player></white> has been cleared";

        public String getGlobalMessage(){
            return this.global;
        }

        public String getServerMessage(){
            return this.server;
        }

        public String getNotFoundServerMessage(){
            return this.serverNotFound;
        }

        public String getPlayerMessage(){
            return this.player;
        }
    }

    @ConfigSerializable
    public static class General{
        @Comment("Violation statistics message\nThis message will appear when using the \"/chatregulator stats\" command.")
        private List<String> stats = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
            "<#3B4371>| <red>General Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>|------------------------|"
        );

        @Comment("Player statistics message\nThis message will appear when using the command \"/chatregulator player <someplayer>\"")
        private List<String> player = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
            "<#3B4371>| <gold><player></gold> <red>Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>|------------------------|"
        );

        @Comment("Message to appear if no arguments are specified in the main command")
        private String info = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>by</aqua> <gradient:green:gold>4drian3d";

        @Comment("Message to send if an invalid argument was entered")
        @Setting(value = "unknown-command")
        private String unknownCommand = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Unknown Command <white><args>";

        @Comment("Message to send when resetting all infractions of a player")
        @Setting(value = "all-reset")
        private String allReset = "<red>The warning count for <player> was reset";

        @Comment("Message to be sent when no argument is entered in a subcommand requiring argument")
        @Setting(value = "without-argument")
        private String withoutArgument = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>No argument provided</white>";

        @Comment("Message to send on not finding the specified user")
        @Setting(value = "player-not-found")
        private String playerNotFound = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>The player <aqua><player></aqua> has not joined the server yet</white>";

        @Comment("Message to send when reloading the plugin configuration")
        @Setting(value = "reload-message")
        private String reloadMessage = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Reloading Configuration";

        @Comment("Help messages")
        @Setting(value = "help-messages")
        private General.Help helpMessages = new General.Help();

        public List<String> getStatsFormat(){
            return this.stats;
        }

        public List<String> getPlayerFormat(){
            return this.player;
        }

        public String getInfoMessage(){
            return this.info;
        }

        public String getUnknowMessage(){
            return this.unknownCommand;
        }

        public String allReset(){
            return this.allReset;
        }

        public String noArgument(){
            return this.withoutArgument;
        }

        public String playerNotFound(){
            return this.playerNotFound;
        }

        public String getReloadMessage(){
            return this.reloadMessage;
        }

        public General.Help getHelpMessages(){
            return this.helpMessages;
        }

        @ConfigSerializable
        public static class Help{
            @Comment("Plugin main help message")
            @Setting(value = "main-help")
            private List<String> main = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <hover:show_text:'<gradient:#ffd89b:#19547b>Click on a section to view its commands'><gradient:#CAC531:#F3F9A7>Command Help</gradient></hover>",
                "<#3B4371>| <hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you the global statistics of infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>stats</aqua></hover>",
                "<#3B4371>| <click:run_command:'/chatr help player'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Obtain the infractions of a specific player</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help reset'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Resets a player infractions</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>reset</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help clear'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Cleans the chat of a player, server or network</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>clear</aqua></hover></click>",
                "<#3B4371>|----------------------|"
            );

            @Comment("Help message for \"/chatregulator player <player>\" command")
            private List<String> player = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Player Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr player <player>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you a player infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua> <player></hover>",
                "<#3B4371>|----------------------|"
            );

            @Comment("Help message for \"/chatregulator reset\" subcommands")
            private List<String> reset = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Reset Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset all infractions of a player</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset all'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset all infractions of a player</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> all</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset infractions'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will restart a player regular infractions.</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> infractions</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset flood'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player flood infractions</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> flood</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset spam'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player spam violations</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> spam</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset command'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player blocked commands executions</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>command</aqua> command</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset unicode'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player unicode violations</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> unicode</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset caps'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player caps violations</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> caps</hover>",
                "<#3B4371>|----------------------|"
            );

            @Comment("Help message for \"/chatregulator clear\" subcommands")
            private List<String> clear = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Clear Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr clear'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear the chat of the entire network</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr clear server <server>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear the chat of an entire server</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua> <green>server <white><server></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr clear player <player>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear a player chat</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua> <green>player <white><player></hover>",
                "<#3B4371>|----------------------|"
            );

            public List<String> getMainHelp(){
                return this.main;
            }

            public List<String> getPlayerHelp(){
                return this.player;
            }

            public List<String> getResethelp(){
                return this.reset;
            }

            public List<String> getClearHelp(){
                return this.clear;
            }
        }
    }
}
