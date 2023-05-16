package io.github._4drian3d.chatregulator.plugin.config;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.api.enums.*;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
public final class Configuration implements Section {
    public static final String HEADER = """
        ChatRegulator | by 4drian3d
        Check the function of each configuration option at
        https://github.com/4drian3d/ChatRegulator/wiki/Configuration""";

    @Comment("Format Module")
    private Format format = new Format();

    @Comment("General Configurations")
    private General general = new General();

    @Comment("CommandSpy configuration")
    private CommandSpy commandSpy = new CommandSpy();

    @Comment("""
        Specify in which commands you want the violations to be detected
        I recommend you to put chat commands, for example: /tell""")
    @Setting(value = "commands-checked")
    private Set<String> commandsChecked = Set.of(
        "tell",
        "etell",
        "msg",
        "emsg",
        "chat",
        "global",
        "reply"
    );

    public Set<String> getCommandsChecked(){
        return this.commandsChecked;
    }

    public Format getFormatConfig(){
        return this.format;
    }

    public General getGeneralConfig(){
        return this.general;
    }

    public CommandSpy getCommandSpyConfig(){
        return this.commandSpy;
    }

    @ConfigSerializable
    public static class Format {
        @Comment("Enable Format Module")
        private boolean enabled = false;

        @Comment("Set the first letter of a sentence in uppercase")
        @Setting(value = "first-letter-uppercase")
        private boolean firstLetterUppercase = true;

        @Comment("Adds a final dot in each sentence")
        @Setting(value = "final-dot")
        private boolean finalDot = true;

        public boolean enabled(){
            return this.enabled;
        }

        public boolean setFirstLetterUppercase(){
            return this.firstLetterUppercase;
        }

        public boolean setFinalDot(){
            return this.finalDot;
        }
    }

    @ConfigSerializable
    public static class CommandSpy {
        @Comment("Enable CommandSpy module")
        private boolean enabled = false;

        @Comment("Commands to ignore")
        private Set<String> ignoredCommands = Set.of(
            "login",
            "register",
            "changepassword"
        );

        public boolean enabled() {
            return this.enabled;
        }

        public boolean shouldAnnounce(CommandSource source, String command){
            return ignoredCommands.contains(Commands.getFirstArgument(command))
                    && Permission.BYPASS_COMMAND_SPY.test(source);
        }
    }

    @ConfigSerializable
    public static class General {
        @Comment("Set the maximum time in which a user's violations will be saved after the user leaves your server")
        @Setting(value = "delete-users-after")
        private long deleteUsersAfter = 30;

        @Comment("""
            Set the time unit of the delete-users-after setting
            Available values: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS""")
        @Setting(value = "time-unit")
        private TimeUnit unit = TimeUnit.SECONDS;

        @Comment("Limit the amount of users showed on autocompletion")
        @Setting(value = "tab-complete-limit")
        private int limitTabComplete = 40;

        public long deleteUsersTime(){
            return this.deleteUsersAfter;
        }

        public TimeUnit unit() {
            return this.unit;
        }

        public int tabCompleteLimit(){
            return this.limitTabComplete;
        }
    }
}
