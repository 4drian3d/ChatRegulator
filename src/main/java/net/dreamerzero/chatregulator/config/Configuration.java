package net.dreamerzero.chatregulator.config;

import java.util.List;

import de.leonhard.storage.Yaml;

/**
 * The configuration paths available in the plugin
 */
public class Configuration {
    private Yaml config;
    private Yaml blacklist;
    private Yaml messages;
    /**
     * Constructor of the Configuration
     * @param config the plugin config
     * @param blacklist the plugin blacklist config
     */
    public Configuration(Yaml config, Yaml blacklist, Yaml messages){
        this.config = config;
        this.blacklist = blacklist;
        this.messages = messages;
    }
    //TODO: Migrate to configurate!!!!!!
    /**
     * Set the default values of the configuration
     * paths if they are not found.
     */
    public void setDefaultConfig(){
        /*---------------
        Blacklist
        ---------------*/
        blacklist.setDefault(
            "blocked-words",
            List.of(
                "f(u|v|4)ck",
                "sh(i|1)t",
                "d(i|1)c(k)?",
                "b(i|1)tch",
                "(a|4|@)w(e|3|@)b(o|@|0)n(a|4|@)d(o|@|0)"));
        /*---------------
        Command Blacklist
        ---------------*/
        blacklist.setDefault(
            "blocked-commands",
            List.of(
                "execute",
                "/calc"));
        config.setDefault(
            "blocked-commands.warning-type",
            "MESSAGE");
        messages.setDefault(
            "blocked-commands.warning",
            "<red>Hello, it is not allowed to use blocked commands");
        messages.setDefault(
            "blocked-commands.alert",
            "<red>The player <aqua><player></aqua> <red>has executed blocked commands in <aqua><server></aqua> server.");
        messages.setDefault(
            "blocked-commands.reset",
            "<red>The infraction warning count for <player> was reset.");
        config.setDefault("blocked-commands.commands.execute-commands", false);
        config.setDefault("blocked-commands.commands.violations-required", 2);
        config.setDefault(
            "blocked-commands.commands.commands-to-execute",
            List.of(
                "mute <player> 1m You have been muted for executing blocked commands",
                "example command"));
        /*---------------
        Infractions
        ---------------*/
        config.setDefault("infractions.enabled", true);
        config.setDefault(
            "infractions.warning-type",
            "MESSAGE");
        messages.setDefault(
            "infractions.warning",
            "<red>Hello, it is not allowed to use dirty words on this server");
        messages.setDefault(
            "infractions.alert",
            "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server.");
        messages.setDefault(
            "infractions.reset",
            "<red>The infraction warning count for <player> was reset.");
        config.setDefault("infractions.control-type", "block");
        config.setDefault("infractions.commands.execute-commands", false);
        config.setDefault("infractions.commands.violations-required", 5);
        config.setDefault(
            "infractions.commands.commands-to-execute",
            List.of(
                "mute <player> 1m You have been muted for swearing on the server <server>",
                "example command"));
        /*---------------
        Flood
        ---------------*/
        config.setDefault("flood.enabled", true);
        config.setDefault(
            "flood.warning-type",
            "MESSAGE");
        messages.setDefault(
            "flood.warning",
            "<red>Hello, it is not allowed to make flood on this server.");
        messages.setDefault(
            "flood.alert",
            "<red>The player <aqua><player></aqua> <red>has make flood in <aqua><server></aqua> server.");
        messages.setDefault(
            "flood.reset",
            "<red>The flood warning count for <player> was reset.");
        config.setDefault("flood.control-type", "block");
        config.setDefault("flood.limit", "5");
        config.setDefault("flood.commands.execute-commands", false);
        config.setDefault("flood.commands.violations-required", 5);
        config.setDefault(
            "flood.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for swearing on the server <server>", "example command"));
        /*---------------
        Spam
        ---------------*/
        config.setDefault("spam.enabled", true);
        config.setDefault("spam.cooldown.enabled", true);
        config.setDefault("spam.cooldown.limit", 2500);
        config.setDefault(
            "spam.warning-type",
            "MESSAGE");
        messages.setDefault(
            "spam.warning",
            "<red>Hello, it is not allowed to make spam on this server.");
        messages.setDefault(
            "spam.reset",
            "<red>The spam warning count for <player> was reset.");
        messages.setDefault(
            "spam.alert",
            "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server.");
        config.setDefault("spam.commands.execute-commands", false);
        config.setDefault("spam.commands.violations-required", 5);
        config.setDefault(
            "spam.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for spam on the server <server>", "example command"));
        /*---------------
        Unicode Module
        ---------------*/
        config.setDefault("unicode-blocker.enabled", true);
        config.setDefault(
            "unicode-blocker.warning-type",
            "MESSAGE");
        messages.setDefault(
            "unicode-blocker.warning",
            "<red>Hello, it is not allowed to use this symbols.");
        messages.setDefault(
            "unicode-blocker.reset",
            "<red>The simbols sended count for <player> was reset.");
        messages.setDefault(
            "unicode-blocker.alert",
            "<red>The player <aqua><player></aqua> <red>was using unicode symbols in the chat of <aqua><server></aqua> server.");
        config.setDefault("unicode-blocker.commands.execute-commands", false);
        config.setDefault("unicode-blocker.commands.violations-required", 5);
        config.setDefault(
            "unicode-blocker.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for use symbols on the server <server>", "example command"));
        /*---------------
        Format Module
        ---------------*/
        config.setDefault("format.enabled", true);
        config.setDefault("format.set-first-letter-uppercase", true);
        config.setDefault("format.set-final-dot", true);
        /*---------------
        Clear Subcommand
        ---------------*/
        messages.setDefault(
            "clear.global-chat-cleared",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat has been cleaned up");
        messages.setDefault(
            "clear.cleared-server-chat",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the server <white><server></white> has been cleared.");
        messages.setDefault(
            "clear.no-server-found",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The <white><server></white> server was not found");
        messages.setDefault(
            "clear.cleared-player-chat",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the player <white><player></white> has been cleared.");
        /*---------------
        General
        ---------------*/
        config.setDefault("general.limit-tab-complete", 30);
        messages.setDefault(
            "general.stats",
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <red>General Stats</red>",
                "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
                "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
                "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
                "<#3B4371>|------------------------|"));
        messages.setDefault(
            "general.player", 
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold><player></gold> <red>Stats</red>",
                "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
                "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
                "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
                "<#3B4371>|------------------------|"));
        messages.setDefault(
            "general.info",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>by</aqua> <gradient:green:gold>4drian3d");
        messages.setDefault(
            "general.unknown-command",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Unknown Command <white><args>");
        messages.setDefault(
            "general.all-reset",
            "<red>The warning count for <player> was reset.");
        messages.setDefault(
            "general.no-argument",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>No argument provided</white>");
        messages.setDefault(
            "general.player-not-found",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>The player <aqua><player></aqua> has not joined the server yet</white>");
        config.setDefault("general.delete-users-after", 300);
        config.setDefault(
            "commands-checked",
            List.of("tell", "etell", "msg", "emsg", "chat", "global", "reply"));
        config.setHeader(
            " ChatRegulator | by 4drian3d",
            " To modify the plugin messages and to use the plugin in general,",
            " I recommend that you have a basic knowledge of MiniMessage.",
            " Guide: https://docs.adventure.kyori.net/minimessage.html#format",
            " Spanish Guide: https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3",
            " ",
            " Check the function of each configuration option at",
            " https://github.com/4drian3d/ChatRegulator/wiki/Configuration");
        /*
        Help Messages
        */
        messages.setDefault(
            "general.help.main",
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <hover:show_text:'<gradient:#ffd89b:#19547b>Click on a section to view its commands'><gradient:#CAC531:#F3F9A7>Command Help</gradient></hover>",
                "<#3B4371>| <hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you the global statistics of infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>stats</aqua></hover>",
                "<#3B4371>| <click:run_command:'/chatr help player'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Obtain the infractions of a specific player</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help reset'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Resets a player infractions</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>reset</aqua></hover></click>",
                "<#3B4371>| <click:run_command:'/chatr help clear'><hover:show_text:'<gradient:#ff4b1f:#ff9068>Cleans the chat of a player, server or network</gradient> <gray>[<yellow>Click here for more</yellow>]'><gradient:#FF5F6D:#FFC371><command> <aqua>clear</aqua></hover></click>",
                "<#3B4371>|----------------------|"
            ));
        messages.setDefault(
            "general.help.player",
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Player Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr player <player>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command shows you a player infractions</gradient>'><gradient:#FF5F6D:#FFC371><command> <aqua>player</aqua> <player></hover>",
                "<#3B4371>|----------------------|"
            ));
        messages.setDefault(
            "general.help.reset",
            List.of(
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
            ));
        messages.setDefault(
            "general.help.clear",
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold>+ <gradient:#CAC531:#F3F9A7>Clear Help</gradient>",
                "<#3B4371>| <click:suggest_command:'/chatr clear'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear the chat of the entire network</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr clear server <server>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear the chat of an entire server</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua> <green>server <white><server></hover>",
                "<#3B4371>| <click:suggest_command:'/chatr clear player <player>'><hover:show_text:'<gradient:#ff4b1f:#ff9068>This command will clear a player chat</gradient>'><gradient:#FF5F6D:#FFC371><command></gradient> <aqua>clear</aqua> <green>player <white><player></hover>",
                "<#3B4371>|----------------------|"
            ));
    }
}
