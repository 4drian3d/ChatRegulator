package net.dreamerzero.chatregulator.config;

import java.util.stream.Collectors;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.MainConfig.Warning;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.title.Title;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public class ConfigManager {
    private MiniMessage mm;
    private Messages.Config messages;
    /**
     * Constructor of the ConfigManager
     */
    public ConfigManager(){
        this.mm = MiniMessage.miniMessage();
        this.messages = Configuration.getMessages();
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param check the flood check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, AbstractCheck check){
        Player player = infractor.getPlayer();
        if(player != null){
            String message = type.getMessages().getWarningMessage();
            PlaceholderResolver placeholder = PlaceholderResolver.combining(
                PlaceholderResolver.placeholders(
                    Placeholder.placeholder("infraction", check.getInfractionWord())),
                    PlaceholderUtils.getPlaceholders(infractor));
            switch(((Warning)type.getConfig()).getWarningType()){
                case TITLE: sendTitle(message, player, placeholder); break;
                case MESSAGE: player.sendMessage(mm.deserialize(message, placeholder)); break;
                case ACTIONBAR: player.sendActionBar(mm.deserialize(message, placeholder)); break;
            }
        }
    }

    private void sendTitle(String message, Audience player, PlaceholderResolver placeholder){
        if(!message.contains(";")){
            player.showTitle(
            Title.title(
                Component.empty(),
                mm.deserialize(
                    message,
                    placeholder)));
        } else {
            String[] titleParts = message.split(";");
            player.showTitle(
                Title.title(
                    mm.deserialize(
                        titleParts[0],
                        placeholder),
                    mm.deserialize(
                        titleParts[1],
                        placeholder)));
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param proxy the proxy
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public void sendAlertMessage(ProxyServer proxy, InfractionPlayer infractor, TypeUtils.InfractionType type){
        Audience staff = Audience.audience(proxy.getAllPlayers().stream()
                .filter(op -> op.hasPermission("chatregulator.notifications"))
                .collect(Collectors.toList()));
        String message = "";
        switch(type){
            case FLOOD: message = messages.getFloodMessages().getAlertMessage(); break;
            case REGULAR: message = messages.getInfractionsMessages().getAlertMessage(); break;
            case SPAM: message = messages.getSpamMessages().getAlertMessage(); break;
            case BCOMMAND:  message = messages.getBlacklistMessages().getAlertMessage(); break;
            case UNICODE: message = messages.getUnicodeMessages().getAlertMessage(); break;
            case CAPS: message = messages.getCapsMessages().getAlertMessage(); break;
            case NONE: return;
        }

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
    public void sendResetMessage(Audience sender, TypeUtils.InfractionType type, InfractionPlayer player){
        switch(type){
            case REGULAR: sender.sendMessage(mm.deserialize(messages.getInfractionsMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
            case FLOOD: sender.sendMessage(mm.deserialize(messages.getFloodMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
            case SPAM: sender.sendMessage(mm.deserialize(messages.getSpamMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
            case NONE: sender.sendMessage(mm.deserialize(messages.getGeneralMessages().allReset(), PlaceholderUtils.getPlaceholders(player))); break;
            case BCOMMAND: sender.sendMessage(mm.deserialize(messages.getBlacklistMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
            case UNICODE: sender.sendMessage(mm.deserialize(messages.getUnicodeMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
            case CAPS: sender.sendMessage(mm.deserialize(messages.getCapsMessages().getResetMessage(), PlaceholderUtils.getPlaceholders(player))); break;
        }
    }
}
