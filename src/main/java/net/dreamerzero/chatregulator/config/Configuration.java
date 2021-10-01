package net.dreamerzero.chatregulator.config;

import net.dreamerzero.chatregulator.Regulator;

import java.util.List;

import de.leonhard.storage.Yaml;

public class Configuration {
    public static void setDefaultConfig(){
        Yaml config = Regulator.getConfig();
        Yaml blacklist = Regulator.getBlackList();

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
        config.setDefault(
            "infractions.warning-type",
            "message");
        config.setDefault(
            "infractions.warning-message",
            "<red>Hello, it is not allowed to use dirty words on this server");
        config.setDefault(
            "infractions.alert-message",
            "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server.");
        config.setDefault("infractions.commands.execute-commands", false);
        config.setDefault("infractions.commands.violations-required", 5);
        config.setDefault(
            "infractions.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for swearing on the server <server>", "example command"));
        /*---------------
        Flood
        ---------------*/
        config.setDefault(
            "flood.warning-type",
            "message");
        config.setDefault(
            "flood.warning-message",
            "<red>Hello, it is not allowed to make flood on this server.");
        config.setDefault(
            "flood.alert-message",
            "<red>The player <aqua><player></aqua> <red>has make flood <aqua><server></aqua> server.");
        config.setDefault("flood.limit", "5");
        config.setDefault("flood.commands.execute-commands", false);
        config.setDefault("flood.commands.violations-required", 5);
        config.setDefault(
            "flood.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for swearing on the server <server>", "example command"));
        /*---------------
        Spam
        ---------------*/
        config.setDefault(
            "spam.warning-type",
            "message");
        config.setDefault(
            "spam.warning-message",
            "<red>Hello, it is not allowed to make flood on this server.");
        config.setDefault(
            "spam.alert-message",
            "<red>The player <aqua><player></aqua> <red>has make flood <aqua><server></aqua> server.");
        //TODO: Implement SPAM limit
        config.setDefault("spam.limit", "5");
        config.setDefault("spam.commands.execute-commands", false);
        config.setDefault("spam.commands.violations-required", 5);
        config.setDefault(
            "spam.commands.commands-to-execute",
            List.of("mute <player> 1m You have been muted for spam on the server <server>", "example command"));
        /*---------------
        General
        ---------------*/
        config.setDefault("debug", false);
        config.setDefault(
            "commands-checked",
            List.of("tell", "etell", "msg", "emsg", "chat", "global", "reply"));
    }

}
