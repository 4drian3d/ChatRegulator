package me.dreamerzero.chatregulator.config;

import java.util.Set;
import java.util.regex.Pattern;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * Blacklist Configuration
 */
public final class Blacklist {
    /**
     * Blacklist configuration subclass
     */
    @ConfigSerializable
    public static class Config{
        @Comment("Sets the expressions to be checked in the\nInfractions module in commands and general chat")
        @Setting(value = "blocked-words")
        private Set<Pattern> blockedPatterns = Set.of(
            Pattern.compile("f(u|v|4)ck", Pattern.CASE_INSENSITIVE),
            Pattern.compile("sh(i|@|l|j|1|y)t", Pattern.CASE_INSENSITIVE),
            Pattern.compile("d(i|@|l|j|1|y)c(k)?", Pattern.CASE_INSENSITIVE),
            Pattern.compile("b(i|@|l|j|1|y)tch", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(a|@|4|x)w(3|@|e|x)b(o|@|0|x|8)n(a|@|4|x)d(o|@|0|x|8)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("p(u|@|v)ssy", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:(?:https?|ftp|file):\\/\\/|www\\.|ftp\\.)(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[-A-Z0-9+&@#\\/%=~_|$?!:,.])*(?:\\([-A-Z0-9+&@#\\/%=~_|$?!:,.]*\\)|[A-Z0-9+&@#\\/%=~_|$])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(i|@|l|j|1|y)mb(3|@|e|x)c(i|@|l|j|1|y)l", Pattern.CASE_INSENSITIVE),
            Pattern.compile("m(o|@|0|x|8)th(3|@|e|x)rf(u|@|v)ck(3|@|e|x)r", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\$\\{(jndi|log4j|sys|env|main|marker|java|base64|lower|upper|web|docker|kubernetes|spring|jvmrunargs|date|ctx)\\:.*\\}", Pattern.CASE_INSENSITIVE)
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

        /**
         * Get the blocked regex strings
         * @return the blocked regex strings
         */
        public Set<Pattern> getBlockedPatterns(){
            return this.blockedPatterns;
        }

        /**
         * Get the blocked commands
         * @return the blocked commands
         */
        public Set<String> getBlockedCommands(){
            return this.blockedCommands;
        }
    }

    private Blacklist(){}
}
