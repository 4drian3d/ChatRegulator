package net.dreamerzero.chatregulator.config;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import net.kyori.adventure.title.Title;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public class ConfigManager {
    private MiniMessage mm;
    private MainConfig.Config config;
    private Messages.Config messages;
    /**
     * Constructor of the ConfigManager
     */
    public ConfigManager(){
        this.mm = MiniMessage.miniMessage();
        this.config = Configuration.getConfig();
        this.messages = Configuration.getMessages();
    }
    /**
     * Get the warning format according to the configuration
     * @param infraction the type of infraction
     * @return the format of the warning
     */
    public TypeUtils.WarningType getWarningType(TypeUtils.InfractionType infraction){
        switch(infraction){
            case REGULAR: return config.getInfractionsConfig().getWarningType();
            case FLOOD: return config.getFloodConfig().getWarningType();
            case SPAM: return config.getSpamConfig().getWarningType();
            case BCOMMAND: return config.getCommandBlacklistConfig().getWarningType();
            case UNICODE: return config.getUnicodeConfig().getWarningType();
            case CAPS: return config.getCapsConfig().getWarningType();
            case NONE: return TypeUtils.WarningType.MESSAGE;
        }
        return TypeUtils.WarningType.MESSAGE;
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type){
        infractor.getPlayer().ifPresent(player -> {
            String message = messages.getSpamMessages().getWarningMessage();
            TemplateResolver templates = PlaceholderUtils.getTemplates(infractor);

            switch(getWarningType(type)){
                case TITLE: sendTitle(message, player, templates); break;
                case MESSAGE: player.sendMessage(mm.deserialize(message, templates)); break;
                case ACTIONBAR: player.sendActionBar(mm.deserialize(message, templates)); break;
            }
        });
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param fUtils the flood check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, FloodCheck fUtils){
        infractor.getPlayer().ifPresent(player -> {
            String message = messages.getFloodMessages().getWarningMessage();
            TemplateResolver template = TemplateResolver.combining(
                TemplateResolver.templates(
                    Template.template("infraction", fUtils.getInfractionWord())),
                    PlaceholderUtils.getTemplates(infractor));
            switch(getWarningType(type)){
                case TITLE: sendTitle(message, player, template); break;
                case MESSAGE: player.sendMessage(mm.deserialize(message, template)); break;
                case ACTIONBAR: player.sendActionBar(mm.deserialize(message, template)); break;
            }
        });
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param iUtils the infractions check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, InfractionCheck iUtils){
        infractor.getPlayer().ifPresent(player -> {
            String message = messages.getInfractionsMessages().getWarningMessage();
            TemplateResolver template = TemplateResolver.combining(
                TemplateResolver.templates(
                    Template.template("infraction", iUtils.getInfractionWord())),
                    PlaceholderUtils.getTemplates(infractor));
            switch(getWarningType(type)){
                case TITLE: sendTitle(message, player, template); break;
                case MESSAGE: player.sendMessage(mm.deserialize(message, template)); break;
                case ACTIONBAR: player.sendActionBar(mm.deserialize(message, template)); break;
            }
        });
    }

    private void sendTitle(String message, Audience player, TemplateResolver template){
        if(!message.contains(";")){
            player.showTitle(
            Title.title(
                Component.empty(),
                mm.deserialize(
                    message,
                    template)));
        } else {
            String[] titleParts = message.split(";");
            player.showTitle(
                Title.title(
                    mm.deserialize(
                        titleParts[0],
                        template),
                    mm.deserialize(
                        titleParts[1],
                        template)));
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param staff audience that has the required permission to receive the alert
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public void sendAlertMessage(Audience staff, InfractionPlayer infractor, TypeUtils.InfractionType type){
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
                PlaceholderUtils.getTemplates(infractor)));
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
            case REGULAR: sender.sendMessage(mm.deserialize(messages.getInfractionsMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
            case FLOOD: sender.sendMessage(mm.deserialize(messages.getFloodMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
            case SPAM: sender.sendMessage(mm.deserialize(messages.getSpamMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
            case NONE: sender.sendMessage(mm.deserialize(messages.getGeneralMessages().allReset(), PlaceholderUtils.getTemplates(player))); break;
            case BCOMMAND: sender.sendMessage(mm.deserialize(messages.getBlacklistMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
            case UNICODE: sender.sendMessage(mm.deserialize(messages.getUnicodeMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
            case CAPS: sender.sendMessage(mm.deserialize(messages.getCapsMessages().getResetMessage(), PlaceholderUtils.getTemplates(player))); break;
        }
    }
}
