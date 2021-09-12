package net.dreamerzero.chatregulator.config;

import net.dreamerzero.chatregulator.Regulator;

import java.util.List;

public class Configuration {
    public static void setDefaultConfig(){
        Regulator.getBlackList().setDefault(
            "blocked-words",
            List.of(
                "f(u|v|4)ck",
                "sh(i|1)t",
                "d(i|1)c(k)?",
                "b(i|1)tch",
                "(a|4|@)w(e|3|@)b(o|@|0)n(a|4|@)d(o|@|0)"));
        Regulator.getConfig().setDefault(
            "messages.blocked-message",
            "<red>Hello, it is not allowed to use dirty words on this server.");
        Regulator.getConfig().setDefault(
            "messages.flood-message",
            "<red>Hello, it is not allowed to make flood on this server.");
        Regulator.getConfig().setDefault(
            "messages.infraction-detected",
            "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server.");
        Regulator.getConfig().setDefault("flood.limit", "5");
        Regulator.getConfig().setDefault("debug", false);
        Regulator.getConfig().setDefault(
            "commands-checked",
            List.of("tell", "etell", "msg", "emsg", "chat", "global", "reply"));
    }

}
