package net.dreamerzero.chatregulator.config;

import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

public class Messages {
    private Messages(){}
    @ConfigSerializable
    public static class Config{
        @Comment("Configuration of command blacklist module messages")
        private CommandBlacklist blacklist = new CommandBlacklist();

        @Comment("Configuration of regular violation module messages")
        private Infractions infractions = new Infractions();

        @Comment("Configuration of flood module messages")
        private Flood flood = new Flood();

        @Comment("Configuration of spam module messages")
        private Spam spam = new Spam();

        @Comment("Configuration of unicode module messages")
        private Unicode unicode = new Unicode();

        @Comment("Configuration of the messages of the /chatr clear command")
        private Clear clear_chat = new Clear();

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

        public Clear getClearMessages(){
            return this.clear_chat;
        }

        public General getGeneralMessages(){
            return this.general;
        }
    }

    @ConfigSerializable
    public static class CommandBlacklist{
        private String warning = "<red>Hello, it is not allowed to use blocked commands";

        private String alert = "<red>The player <aqua><player></aqua> <red>has executed blocked commands in <aqua><server></aqua> server";

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
        private String warning = "<red>Hello, it is not allowed to use dirty words on this server";

        private String alert = "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server";

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
        private String warning = "<red>Hello, it is not allowed to make flood on this server";

        private String alert = "<red>The player <aqua><player></aqua> <red>has make flood in <aqua><server></aqua> server";

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
        private String warning = "<red>Hello, it is not allowed to make spam on this server";

        private String alert = "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server";

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
        private String warning = "<red>Hello, it is not allowed to use this symbols";

        private String alert = "<red>The player <aqua><player></aqua> <red>was using unicode symbols in the chat of <aqua><server></aqua> server";

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
        private String global = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat has been cleaned up";
        private String server = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the server <white><server></white> has been cleared";
        private String server_not_found = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The <white><server></white> server was not found";
        private String player = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the player <white><player></white> has been cleared";

        public String getGlobalMessage(){
            return this.global;
        }

        public String getServerMessage(){
            return this.server;
        }

        public String getNotFoundServerMessage(){
            return this.server_not_found;
        }

        public String getPlayerMessage(){
            return this.player;
        }
    }

    @ConfigSerializable
    public static class General{
        private List<String> stats = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
            "<#3B4371>| <red>General Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>|------------------------|"
        );

        private List<String> player = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
            "<#3B4371>| <gold><player></gold> <red>Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>|------------------------|"
        );

        private String info = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>by</aqua> <gradient:green:gold>4drian3d";

        private String unknown_command = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Unknown Command <white><args>";

        private String all_reset = "<red>The warning count for <player> was reset";

        private String without_argument = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>No argument provided</white>";

        private String player_not_found = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>The player <aqua><player></aqua> has not joined the server yet</white>";

        private General.Help help_messages = new General.Help();

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
            return this.unknown_command;
        }

        public String allReset(){
            return this.all_reset;
        }

        public String noArgument(){
            return this.without_argument;
        }

        public String playerNotFound(){
            return this.player_not_found;
        }

        public General.Help getHelpMessages(){
            return this.help_messages;
        }

        @ConfigSerializable
        public static class Help{
            private List<String> main = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <hover:show_text:'<gradient:#ffd89b:#19547b>Click on a section to view its commands'><gradient:#CAC531:#F3F9A7>Command Help</gradient></hover>",
                "<#3B4371>| <hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you the global statistics of infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>stats</aqua></hover>",
                "<#3B4371>| <click:run_command:'/chatr help player'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Obtain the infractions of a specific player</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help reset'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Resets a player infractions</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>reset</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help clear'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Cleans the chat of a player, server or network</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>clear</aqua></hover></click>",
                "<#3B4371>|----------------------|"
            );

            private List<String> player = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Player Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr player <player>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you a player infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua> <player></hover>",
                "<#3B4371>|----------------------|"
            );

            private List<String> reset = List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Reset Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset all infractions of a player</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset all'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset all infractions of a player</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> all</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset infractions'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will restart a player regular infractions.</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> infractions</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset flood'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player flood infractions</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> flood</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset spam'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player spam violations</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> spam</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset command'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player blocked commands executions</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>command</aqua> spam</hover>",
                "<#3B4371>| <click:suggest_command:'/chatr <player> reset unicode'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will reset a player unicode violations</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <green><player> <aqua>reset</aqua> spam</hover>",
                "<#3B4371>|----------------------|"
            );

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
