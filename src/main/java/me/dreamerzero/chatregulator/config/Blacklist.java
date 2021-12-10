package me.dreamerzero.chatregulator.config;

import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * Blacklist Configuration
 */
public class Blacklist {
    @ConfigSerializable
    public static class Config{
        @Comment("Sets the expressions to be checked in the\nInfractions module in commands and general chat")
        @Setting(value = "blocked-words")
        private Set<String> blockedWords = Set.of(
            "f(u|v|4)ck",
            "sh(i|@|l|j|1|y)t",
            "d(i|@|l|j|1|y)c(k)?",
            "b(i|@|l|j|1|y)tch",
            "(a|@|4|x)w(3|@|e|x)b(o|@|0|x|8)n(a|@|4|x)d(o|@|0|x|8)",
            "p(u|@|v)ssy",
            "(?:(?:https?|ftp|file):\\/\\/|www\\.|ftp\\.)(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[-A-Z0-9+&@#\\/%=~_|$?!:,.])*(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[A-Z0-9+&@#\\/%=~_|$])",
            "(i|@|l|j|1|y)mb(3|@|e|x)c(i|@|l|j|1|y)l",
            "m(o|@|0|x|8)th(3|@|e|x)rf(u|@|v)ck(3|@|e|x)r",
            "\\$\\{(jndi|log4j|sys|env|main|marker|java|base64|lower|upper|web|docker|kubernetes|spring|jvmrunargs|date|ctx)\\:.*\\}"
        );

        @Comment("Sets the commands that cannot be executed\n(configurable in the command module)")
        @Setting(value = "blocked-commands")
        private Set<String> blockedCommands = Set.of(
            "execute",
            "/calc",
            "/calculate",
            "/solve",
            "/eval",
            "pex",
            "mv",
            "multiverse"
        );

        public Set<String> getBlockedWord(){
            return this.blockedWords;
        }

        public Set<String> getBlockedCommands(){
            return this.blockedCommands;
        }
    }

    private Blacklist(){}
}
