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
        /*---------------
        General
        ---------------*/
        config.setDefault("debug", false);
        config.setDefault(
            "commands-checked",
            List.of("tell", "etell", "msg", "emsg", "chat", "global", "reply"));
    }

}
