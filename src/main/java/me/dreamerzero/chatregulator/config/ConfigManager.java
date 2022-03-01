package me.dreamerzero.chatregulator.config;

import java.util.stream.Collectors;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
    public static void sendWarningMessage(InfractionPlayer infractor, Result result, Messages.Warning messages, MainConfig.Warning config){
        String message = messages.getWarningMessage();
        TagResolver placeholder = TagResolver.resolver(
            Placeholder.unparsed("infraction", result.getInfractionString()),
            PlaceholderUtils.getPlaceholders(infractor));
        switch(config.getWarningType()){
            case TITLE: sendTitle(message, infractor, placeholder); break;
            case MESSAGE: infractor.sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(message, placeholder)); break;
            case ACTIONBAR: infractor.sendActionBar(Components.SPECIAL_MINIMESSAGE.deserialize(message, placeholder)); break;
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
    public static void sendAlertMessage(InfractionPlayer infractor, InfractionType type){
        String message = "";
        Messages.Config messages = Configuration.getMessages();
        MiniMessage mm = Components.MESSAGE_MINIMESSAGE;
        switch(type){
            case FLOOD: message = messages.getFloodMessages().getAlertMessage(); break;
            case REGULAR: message = messages.getInfractionsMessages().getAlertMessage(); break;
            case SPAM: message = messages.getSpamMessages().getAlertMessage(); break;
            case BCOMMAND:  message = messages.getBlacklistMessages().getAlertMessage(); break;
            case UNICODE: message = messages.getUnicodeMessages().getAlertMessage(); break;
            case CAPS: message = messages.getCapsMessages().getAlertMessage(); break;
            case SYNTAX: message = messages.getSyntaxMessages().getAlertMessage(); break;
            case NONE: return;
        }

        Audience staff = Audience.audience(ChatRegulator.getInstance().getProxy().getAllPlayers().stream()
                .filter(op -> op.hasPermission(Permissions.NOTIFICATIONS))
                .collect(Collectors.toList()));

        staff.sendMessage(
            mm.deserialize(
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
    public static void sendResetMessage(Audience sender, InfractionType type, InfractionPlayer player){
        Messages.Config messages = Configuration.getMessages();
        MiniMessage mm = Components.MESSAGE_MINIMESSAGE;
        TagResolver resolver = PlaceholderUtils.getPlaceholders(player);
        switch(type){
            case REGULAR: sender.sendMessage(mm.deserialize(messages.getInfractionsMessages().getResetMessage(), resolver)); break;
            case FLOOD: sender.sendMessage(mm.deserialize(messages.getFloodMessages().getResetMessage(), resolver)); break;
            case SPAM: sender.sendMessage(mm.deserialize(messages.getSpamMessages().getResetMessage(), resolver)); break;
            case NONE: sender.sendMessage(mm.deserialize(messages.getGeneralMessages().allReset(), resolver)); break;
            case BCOMMAND: sender.sendMessage(mm.deserialize(messages.getBlacklistMessages().getResetMessage(), resolver)); break;
            case UNICODE: sender.sendMessage(mm.deserialize(messages.getUnicodeMessages().getResetMessage(), resolver)); break;
            case CAPS: sender.sendMessage(mm.deserialize(messages.getCapsMessages().getResetMessage(), resolver)); break;
            case SYNTAX: sender.sendMessage(mm.deserialize(messages.getSyntaxMessages().getResetMessage(), resolver)); break;
        }
    }
}
