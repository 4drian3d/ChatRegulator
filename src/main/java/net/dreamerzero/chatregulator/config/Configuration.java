package net.dreamerzero.chatregulator.config;

import net.dreamerzero.chatregulator.Regulator;

import java.util.List;

public class Configuration {
    public static void setDefaultConfig(){
        var config = Regulator.getConfig();
        var blacklist = Regulator.getBlackList();

        blacklist.setDefault(
            "blocked-words",
            List.of(
                "f(u|v|4)ck",
                "sh(i|1)t",
                "d(i|1)c(k)?",
                "b(i|1)tch",
                "(a|4|@)w(e|3|@)b(o|@|0)n(a|4|@)d(o|@|0)"));
        config.setDefault(
            "messages.blocked-message",
            "<red>Hello, it is not allowed to use dirty words on this server.");
        config.setDefault(
            "messages.flood-message",
            "<red>Hello, it is not allowed to make flood on this server.");
        config.setDefault(
            "messages.infraction-detected",
            "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server.");
        config.setDefault("flood.limit", "5");
        config.setDefault("debug", false);
        config.setDefault(
            "commands-checked",
            List.of("tell", "etell", "msg", "emsg", "chat", "global", "reply"));
    }

}
