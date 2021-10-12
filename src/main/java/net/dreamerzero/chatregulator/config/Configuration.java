package net.dreamerzero.chatregulator.config;

import java.util.List;

import de.leonhard.storage.Yaml;

/**
 * The configuration paths available in the plugin
 */
public class Configuration {
    private Yaml config;
    private Yaml blacklist;
    /**
     * Constructor of the Configuration
     * @param config the plugin config
     * @param blacklist the plugin blacklist config
     */
    public Configuration(Yaml config, Yaml blacklist){
        this.config = config;
        this.blacklist = blacklist;
    }
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
        Infractions
        ---------------*/
        config.setDefault("infractions.enabled", true);
        config.setDefault(
            "infractions.warning-type",
            "message");
        config.setDefault(
            "infractions.messages.warning",
            "<red>Hello, it is not allowed to use dirty words on this server");
        config.setDefault(
            "infractions.messages.alert",
            "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server.");
        config.setDefault(
            "infractions.messages.reset",
            "<red>The infraction warning count for <player> was reset.");
        config.setDefault("infractions.commands.execute-commands", false);
        config.setDefault("infractions.commands.violations-required", 5);
        config.setDefault(
            "infractions.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for swearing on the server <server>", "example command"));
        /*---------------
        Flood
        ---------------*/
        config.setDefault("flood.enabled", true);
        config.setDefault(
            "flood.warning-type",
            "message");
        config.setDefault(
            "flood.messages.warning",
            "<red>Hello, it is not allowed to make flood on this server.");
        config.setDefault(
            "flood.messages.alert",
            "<red>The player <aqua><player></aqua> <red>has make flood in <aqua><server></aqua> server.");
        config.setDefault(
            "flood.messages.reset",
            "<red>The flood warning count for <player> was reset.");
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
        config.setDefault(
            "spam.warning-type",
            "message");
        config.setDefault(
            "spam.messages.warning",
            "<red>Hello, it is not allowed to make spam on this server.");
        config.setDefault(
            "spam.messages.reset",
            "<red>The spam warning count for <player> was reset.");
        config.setDefault(
            "spam.messages.alert",
            "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server.");
        config.setDefault("spam.commands.execute-commands", false);
        config.setDefault("spam.commands.violations-required", 5);
        config.setDefault(
            "spam.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for spam on the server <server>", "example command"));
        /*---------------
        Format Module
        ---------------*/
        config.setDefault("format.enabled", true);
        config.setDefault("format.set-first-letter-uppercase", true);
        config.setDefault("format.set-final-dot", true);
        /*
        Clear 
        */
        config.setDefault("clear.messages.global-chat-cleared", "The chat has been cleaned up");
        config.setDefault("clear.messages.cleared-server-chat", "The chat of the player <player> has been cleared.");
        config.setDefault("clear.messages.no-server-found", "The <server> server was not found");
        /*---------------
        General
        ---------------*/
        config.setDefault("general.debug", false);
        config.setDefault("general.limit-tab-complete", 30);
        config.setDefault(
            "general.messages.stats",
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <red>General Stats</red>",
                "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
                "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
                "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
                "<#3B4371>|------------------------|"));
        config.setDefault(
            "general.messages.player", 
            List.of(
                "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------|",
                "<#3B4371>| <gold><player></gold> <red>Stats</red>",
                "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regular></white>",
                "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
                "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
                "<#3B4371>|------------------------|"));
        config.setDefault(
            "general.messages.info",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>by</aqua> <gradient:green:gold>4drian3d");
        config.setDefault(
            "general.messages.unknown-command",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Unknown Command <white><args>");
        config.setDefault(
            "general.messages.all-reset",
            "<red>The warning count for <player> was reset.");
        config.setDefault(
            "general.messages.no-argument",
            "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>No argument provided</white>");
        config.setDefault(
            "general.messages.player-not-found",
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
    }

}
