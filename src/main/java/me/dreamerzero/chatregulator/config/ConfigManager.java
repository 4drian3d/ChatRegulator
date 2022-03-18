package me.dreamerzero.chatregulator.config;

import java.util.stream.Collectors;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Messages.Alert;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public class ConfigManager {
    private ConfigManager(){}
    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param result the result of the infraction
     * @param messages the messages
     */
    public static void sendWarningMessage(InfractionPlayer infractor, Result result, InfractionType type, IFormatter formatter){
        String message = type.getMessages().get().getWarningMessage();
        TagResolver placeholder = TagResolver.resolver(
            Placeholder.unparsed("infraction", result.getInfractionString()),
            PlaceholderUtils.getPlaceholders(infractor));
        switch(((MainConfig.Warning)type.getConfig().get()).getWarningType()){
            case TITLE: sendTitle(message, infractor, placeholder); break;
            case MESSAGE: infractor.sendMessage(formatter.parse(message, infractor.getPlayer(), placeholder)); break;
            case ACTIONBAR: infractor.sendActionBar(formatter.parse(message, infractor.getPlayer(), placeholder)); break;
        }
    }

    private static void sendTitle(String message, Audience player, TagResolver placeholder){
        if(message.indexOf(';') != -1){
            player.showTitle(
            Title.title(
                Component.empty(),
                Components.SPECIAL_MINIMESSAGE.deserialize(
                    message,
                    placeholder)));
        } else {
            String[] titleParts = message.split(";");
            player.showTitle(
                Title.title(
                    Components.SPECIAL_MINIMESSAGE.deserialize(
                        titleParts[0],
                        placeholder),
                    Components.SPECIAL_MINIMESSAGE.deserialize(
                        titleParts[1],
                        placeholder)));
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public static void sendAlertMessage(InfractionPlayer infractor, InfractionType type, ChatRegulator plugin){
        String message = ((Alert)type.getMessages().get()).getAlertMessage();

        Audience staff = Audience.audience(plugin.getProxy().getAllPlayers().stream()
                .filter(op -> op.hasPermission(Permissions.NOTIFICATIONS))
                .collect(Collectors.toList()));

        staff.sendMessage(
            plugin.getFormatter().parse(
                message,
                PlaceholderUtils.getPlaceholders(infractor)));
    }

    /**
     * Sends the message of a successful
     * warning reset to the command executor
     * @param sender command executor
     * @param type type of infraction
     * @param player the infraction player
     *               whose warnings have been reset
     */
    public static void sendResetMessage(Audience sender, InfractionType type, InfractionPlayer player, IFormatter formatter){
        Messages.Config messages = Configuration.getMessages();
        if(sender instanceof InfractionPlayer){
            InfractionPlayer p = ((InfractionPlayer)sender);
            if(p.isOnline()) sender = p.getPlayer();
        }
        TagResolver resolver = PlaceholderUtils.getPlaceholders(player);
        switch(type){
            case REGULAR: sender.sendMessage(formatter.parse(messages.getInfractionsMessages().getResetMessage(), sender, resolver)); break;
            case FLOOD: sender.sendMessage(formatter.parse(messages.getFloodMessages().getResetMessage(), sender, resolver)); break;
            case SPAM: sender.sendMessage(formatter.parse(messages.getSpamMessages().getResetMessage(), sender, resolver)); break;
            case NONE: sender.sendMessage(formatter.parse(messages.getGeneralMessages().allReset(), sender, resolver)); break;
            case BCOMMAND: sender.sendMessage(formatter.parse(messages.getBlacklistMessages().getResetMessage(), sender, resolver)); break;
            case UNICODE: sender.sendMessage(formatter.parse(messages.getUnicodeMessages().getResetMessage(), sender, resolver)); break;
            case CAPS: sender.sendMessage(formatter.parse(messages.getCapsMessages().getResetMessage(), sender, resolver)); break;
            case SYNTAX: sender.sendMessage(formatter.parse(messages.getSyntaxMessages().getResetMessage(), sender, resolver)); break;
        }
    }
}
