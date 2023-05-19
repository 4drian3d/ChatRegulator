package io.github._4drian3d.chatregulator.plugin.config;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import static io.github._4drian3d.chatregulator.api.utils.Commands.getFirstArgument;

/**
 * Blacklist Configuration
 */
@ConfigSerializable
public final class Blacklist implements Section {
    public static final String HEADER = """
        ChatRegulator | by 4drian3d
        Blacklist of Commands and Regular Expressions
        To test each regular expression, use:
        https://regex101.com/
        If you are using patterns that include '\\', replace them with '\\\\'""";

    @Comment("""
        Sets the expressions to be checked in the
        Infractions module in commands and general chat""")
    @Setting(value = "blocked-words")
    private Pattern[] blockedPatterns = {
            Pattern.compile("f[uv4@]ck", Pattern.CASE_INSENSITIVE),
            Pattern.compile("sh[i@lj1y]t", Pattern.CASE_INSENSITIVE),
            Pattern.compile("d[i@lj1y]c(k)?", Pattern.CASE_INSENSITIVE),
            Pattern.compile("b[i@lj1y]tch", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[a@4x]w[3@ex]b[o@0x8]n[a@4x]d[o@0x8]", Pattern.CASE_INSENSITIVE),
            Pattern.compile("p[u@v]ssy", Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:(?:https?|ftp|file)://|www\\.|ftp\\.)(?:\\([-A-Z0-9+&@#/%=~_|$?!:,.]*\\)|[-A-Z0-9+&@#/%=~_|$?!:,.])*(?:\\([-A-Z0-9+&@#/%=~_|$?!:,.]*\\)|[A-Z0-9+&@#/%=~_|$])", Pattern.CASE_INSENSITIVE),
            Pattern.compile("[i@lj1y]mb[3@ex]c[i@lj1y]l", Pattern.CASE_INSENSITIVE),
            Pattern.compile("m[o@0x8]th[3@ex]rf[u@v]ck[3@ex]r", Pattern.CASE_INSENSITIVE)
    };

    @Comment("""
        Sets the commands that cannot be executed
        (configurable in the command module)""")
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
    public Pattern[] getBlockedPatterns(){
        return this.blockedPatterns;
    }

    /**
     * Get the blocked commands
     * @return the blocked commands
     */
    public Set<String> getBlockedCommands(){
        return this.blockedCommands;
    }

    public boolean isBlockedCommand(String command) {
        final String firstArgument = getFirstArgument(Objects.requireNonNull(command));
        return getBlockedCommands().stream()
                .anyMatch(firstArgument::equalsIgnoreCase);
    }
}
