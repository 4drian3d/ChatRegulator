package me.dreamerzero.chatregulator.config;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Messages.Alert;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permission;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public final class ConfigManager {
    private ConfigManager(){}
    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param result the result of the infraction
     * @param type the infraction type
     * @param plugin the plugin
     */
    public static void sendWarningMessage(InfractionPlayer infractor, Result result, InfractionType type, ChatRegulator plugin){
        final String message = type.getMessages(plugin.getMessages()).getWarningMessage();
        final TagResolver placeholder = TagResolver.resolver(
            Placeholder.unparsed("infraction", result.getInfractionString()),
            plugin.placeholders().getPlaceholders(infractor));
        ((Configuration.Warning)type.getConfig(plugin.getConfig())).getWarningType()
            .send(message, infractor, placeholder, plugin.getFormatter());
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public static void sendAlertMessage(
        final InfractionPlayer infractor,
        final InfractionType type,
        final ChatRegulator plugin,
        final Result result
    ) {
        final Component message = plugin.getFormatter().parse(
            ((Alert)type.getMessages(plugin.getMessages())).getAlertMessage(),
            TagResolver.resolver(
                plugin.placeholders().getPlaceholders(infractor),
                Placeholder.unparsed("string", result.getInfractionString()))
        );

        plugin.getProxy().getAllPlayers().forEach(player -> {
            if(Permission.NOTIFICATIONS.test(player)) {
                player.sendMessage(message);
            }
        });

        plugin.getProxy().getConsoleCommandSource().sendMessage(message);
    }

    /**
     * Sends the message of a successful
     * warning reset to the command executor
     * @param sender command executor
     * @param type type of infraction
     * @param player the infraction player
     *               whose warnings have been reset
     */
    public static void sendResetMessage(Audience sender, InfractionType type, InfractionPlayer player, ChatRegulator plugin){
        Messages messages = plugin.getMessages();
        if(sender instanceof InfractionPlayer p && p.isOnline()){
            sender = p.getPlayer();
        }
        final TagResolver resolver = plugin.placeholders().getPlaceholders(player);
        sender.sendMessage(switch(type){
            case REGULAR -> plugin.getFormatter().parse(messages.getInfractionsMessages().getResetMessage(), sender, resolver);
            case FLOOD -> plugin.getFormatter().parse(messages.getFloodMessages().getResetMessage(), sender, resolver);
            case SPAM -> plugin.getFormatter().parse(messages.getSpamMessages().getResetMessage(), sender, resolver);
            case NONE -> plugin.getFormatter().parse(messages.getGeneralMessages().allReset(), sender, resolver);
            case BCOMMAND -> plugin.getFormatter().parse(messages.getBlacklistMessages().getResetMessage(), sender, resolver);
            case UNICODE -> plugin.getFormatter().parse(messages.getUnicodeMessages().getResetMessage(), sender, resolver);
            case CAPS -> plugin.getFormatter().parse(messages.getCapsMessages().getResetMessage(), sender, resolver);
            case SYNTAX -> plugin.getFormatter().parse(messages.getSyntaxMessages().getResetMessage(), sender, resolver);
        });
    }
}
