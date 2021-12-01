package net.dreamerzero.chatregulator.config;

import java.util.Set;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public class Blacklist {
    @ConfigSerializable
    public static class Config{
        @Comment("Sets the expressions to be checked in the\nInfractions module in commands and general chat")
        @Setting(value = "blocked-words")
        private Set<String> blockedWords = Set.of(
            "f(u|v|4)ck",
            "sh(i|1)t",
            "d(i|1)c(k)?",
            "b(i|1)tch",
            "(a|4|@)w(e|3|@)b(o|@|0)n(a|4|@)d(o|@|0)"
        );

        @Comment("Sets the commands that cannot be executed\n(configurable in the command module)")
        @Setting(value = "blocked-commands")
        private Set<String> blockedCommands = Set.of(
            "execute",
            "/calc"
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
