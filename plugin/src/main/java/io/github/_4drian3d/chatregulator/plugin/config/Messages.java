package io.github._4drian3d.chatregulator.plugin.config;

import java.util.List;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class Messages implements Section {
    public static final String HEADER = """
        ChatRegulator | by 4drian3d
        To modify the plugin messages and to use the plugin in general
        I recommend that you have a basic knowledge of MiniMessage
        Guide: https://docs.adventure.kyori.net/minimessage.html#format
        Spanish Guide: https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3""";

    @Comment("Configuration of command blacklist module messages")
    @Setting(value = "command-blacklist")
    private CommandBlacklist blacklist = new CommandBlacklist();

    @Comment("Configuration of regex violation module messages")
    private Regex infractions = new Regex();

    @Comment("Configuration of flood module messages")
    private Flood flood = new Flood();

    @Comment("Configuration of spam module messages")
    private Spam spam = new Spam();

    @Comment("Configuration of cooldown module messages")
    private Cooldown cooldown = new Cooldown();

    @Comment("Configuration of unicode module messages")
    private Unicode unicode = new Unicode();

    @Comment("Configuration of caps module messages")
    private Caps caps = new Caps();

    @Comment("Configuration of syntax blocker module messages")
    private Syntax syntax = new Syntax();

    @Comment("Configuration of the messages of the /chatr clear command")
    @Setting(value = "clear-chat")
    private Clear clearChat = new Clear();

    @Comment("Configuration of the messages of the command spy module")
    private CommandSpy commandSpy = new CommandSpy();

    @Comment("General Messages")
    private General general = new General();

    public Regex getRegexMessages(){
        return this.infractions;
    }

    public CommandBlacklist getBlacklistMessages(){
        return this.blacklist;
    }

    public Flood getFloodMessages(){
        return this.flood;
    }

    public Spam getSpamMessages(){
        return this.spam;
    }

    public Unicode getUnicodeMessages(){
        return this.unicode;
    }

    public Caps getCapsMessages(){
        return this.caps;
    }

    public Cooldown getCooldownMessages() {
        return this.cooldown;
    }

    public Syntax getSyntaxMessages(){
        return this.syntax;
    }

    public Clear getClearMessages(){
        return this.clearChat;
    }

    public CommandSpy getCommandSpyMessages(){
        return this.commandSpy;
    }

    public General getGeneralMessages(){
        return this.general;
    }

    @ConfigSerializable
    public static class CommandBlacklist implements Warning, Alert, Reset{
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to use blocked commands";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has executed blocked commands in <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The commands infraction count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Regex implements Warning, Alert, Reset{
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to use dirty words on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has said forbidden words in <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The infraction warning count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Flood implements Warning, Alert, Reset{
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to make flood on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>has make flood in <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The flood warning count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Spam implements Warning, Alert, Reset {
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to make spam on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The spam warning count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Cooldown implements Warning, Alert, Reset {
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to make spam on this server";

        @Comment("Message to be sent to staff with chatregulator.notifications permission") // TODO: Change msg
        private String alert = "<red>The player <aqua><player></aqua> <red>was spamming the chat in <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The spam warning count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Unicode implements Warning, Alert, Reset{
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to use this symbols";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was using unicode symbols in the chat of <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The simbols sended count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Caps implements Warning, Alert, Reset {
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to use too many caps";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was using many caps in the chat of <aqua><server></aqua> server | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The caps violation count for <player> was reset";

        @Override
        public String getWarningMessage(){
            return this.warning;
        }

        @Override
        public String getAlertMessage(){
            return this.alert;
        }

        @Override
        public String getResetMessage(){
            return this.reset;
        }
    }

    @ConfigSerializable
    public static class Syntax implements Warning, Alert, Reset{
        @Comment("""
            Message to be sent to the offender
            Depending on your warning-type section settings, it will be sent as Title, Actionbar or Message
            In case you use the Title mode, put a ; to delimit the title and the subtitle""")
        private String warning = "<red>Hello, it is not allowed to use this type of commands";

        @Comment("Message to be sent to staff with chatregulator.notifications permission")
        private String alert = "<red>The player <aqua><player></aqua> <red>was using commands with \"/command:subcommand\" syntax in the server <aqua><server></aqua> | String: <string>";

        @Comment("Statistics Reset Confirmation Message")
        private String reset = "<red>The syntax violation count for <player> was reset";

        @Override
        public String getResetMessage() {
            return this.reset;
        }

        @Override
        public String getAlertMessage() {
            return this.alert;
        }

        @Override
        public String getWarningMessage() {
            return this.warning;
        }

    }

    @ConfigSerializable
    public static class Clear{
        @Comment("Confirmation of global chat cleanup")
        private String global = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat has been cleaned up";

        @Comment("Confirmation of chat cleanup on a server")
        private String server = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the server <white><server></white> has been cleared";

        @Comment("Message to send if a server was not found")
        @Setting(value = "server-not-fount")
        private String serverNotFound = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The <white><server></white> server was not found";

        @Comment("Confirmation of chat clearing for a user")
        private String player = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>The chat of the player <white><player></white> has been cleared";
        
        public String getGlobalMessage(){
            return this.global;
        }
        
        public String getServerMessage(){
            return this.server;
        }
        
        public String getNotFoundServerMessage(){
            return this.serverNotFound;
        }
        
        public String getPlayerMessage(){
            return this.player;
        }
    }

    @ConfigSerializable
    public static class CommandSpy {
        @Comment("Message to send")
        private String message = "<gradient:red:yellow>CommandSpy</gradient> <white>| <aqua><player> <white>| <gray><command>";

        public String getMessage(){
            return this.message;
        }
    }

    @ConfigSerializable
    public static class General implements Reset {
        @Comment("""
            Violation statistics message
            This message will appear when using the "/chatregulator stats" command
            Available Placeholders: <flood>, <spam>, <regex>, <command>, <unicode>, <caps>. <syntax>""")
        private List<String> stats = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------| ",
            "<#3B4371>| <red>General Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regex></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>| <aqua>Caps Infractions:</aqua> <white><caps></white>",
            "<#3B4371>| <aqua>Unicode Infractions:</aqua> <white><unicode></white>",
            "<#3B4371>| <aqua>Command Infractions:</aqua> <white><blocked_command></white>",
            "<#3B4371>| <aqua>Syntax Infractions:</aqua> <white><syntax></white>",
            "<#3B4371>|------------------------|"
        );

        @Comment("""
            Player statistics message
            This message will appear when using the command "/chatregulator player <someplayer>"
            Available Placeholders: <player>, <flood>, <spam>, <regex>, <unicode>, <caps>, <syntax>""")
        private List<String> player = List.of(
            "<#3B4371>|-- <gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> -------| ",
            "<#3B4371>| <gold><player></gold> <red>Stats</red>",
            "<#3B4371>| <aqua>Regular Infractions:</aqua> <white><regex></white>",
            "<#3B4371>| <aqua>Flood Infractions:</aqua> <white><flood></white>",
            "<#3B4371>| <aqua>Spam Infractions:</aqua> <white><spam></white>",
            "<#3B4371>| <aqua>Unicode Infractions:</aqua> <white><unicode></white>",
            "<#3B4371>| <aqua>Caps Infractions:</aqua> <white><caps></white>",
            "<#3B4371>| <aqua>Command Infractions:</aqua> <white><blocked_command></white>",
            "<#3B4371>| <aqua>Syntax Infractions:</aqua> <white><syntax></white>",
            "<#3B4371>|------------------------|"
        );

        @Comment("Message to appear if no arguments are specified in the main command")
        private String info = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>by</aqua> <gradient:green:gold>4drian3d";

        @Comment("Message to send when resetting all infractions of a player")
        @Setting(value = "all-reset")
        private String allReset = "<red>The infractions count for <player> was reset";

        @Comment("Message to be sent when no argument is entered in a subcommand requiring argument")
        @Setting(value = "without-argument")
        private String withoutArgument = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>No argument provided</white>";

        @Comment("Message to send on not finding the specified user")
        @Setting(value = "player-not-found")
        private String playerNotFound = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <white>The player <aqua><player></aqua> has not joined the server yet</white>";

        @Comment("Message to send when reloading the plugin configuration")
        @Setting(value = "reload-message")
        private String reloadMessage = "<gradient:#67B26F:#4ca2cd>ChatRegulator</gradient> <aqua>Reloading Configuration";

        @Comment("Suggestion format to send on '/chatr reset' command")
        @Setting(value = "player-suggestion-format")
        private String playerSuggestionFormat = "Reset <player> infractions";

        public List<String> getStatsFormat(){
            return this.stats;
        }

        public List<String> getPlayerFormat(){
            return this.player;
        }

        public String getInfoMessage(){
            return this.info;
        }

        public String noArgument(){
            return this.withoutArgument;
        }

        public String playerNotFound(){
            return this.playerNotFound;
        }

        public String getReloadMessage(){
            return this.reloadMessage;
        }

        public String getPlayerSuggestionsFormat(){
            return this.playerSuggestionFormat;
        }

        @Override
        public String getResetMessage() {
            return this.allReset;
        }
    }

    public interface Warning{
        String getWarningMessage();
    }

    public interface Reset{
        String getResetMessage();
    }

    public interface Alert{
        String getAlertMessage();
    }

    private Object getMessages(InfractionType type) {
        return switch (type) {
            case REGEX -> getRegexMessages();
            case FLOOD -> getFloodMessages();
            case SPAM -> getSpamMessages();
            case COOLDOWN -> getCooldownMessages();
            case BLOCKED_COMMAND -> getBlacklistMessages();
            case UNICODE -> getUnicodeMessages();
            case CAPS -> getCapsMessages();
            case SYNTAX -> getSyntaxMessages();
            case GLOBAL -> getGeneralMessages();
        };
    }

    public Reset getReset(InfractionType type) {
        Object messages = getMessages(type);
        return (Reset) messages;
    }

    public Warning getWarning(InfractionType type) {
        Object messages = getMessages(type);
        return messages == null
                ?  null : (Warning) messages;
    }

    public Alert getAlert(InfractionType type) {
        Object messages = getMessages(type);
        return messages == null
                ?  null : (Alert) messages;
    }
}
