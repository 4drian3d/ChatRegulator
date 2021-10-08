package net.dreamerzero.chatregulator.config;

import java.util.ArrayList;
import java.util.List;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.title.Title;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public class ConfigManager {
    private Yaml config;
    /**
     * Constructor of the ConfigManager
     * @param config plugin config
     */
    public ConfigManager(Yaml config){
        this.config = config;
    }
    /**
     * Get the warning format according to the configuration
     * @param infraction the type of infraction
     * @return the format of the warning
     */
    public TypeUtils.WarningType getWarningType(TypeUtils.InfractionType infraction){
        String type;

        switch(infraction){
            case REGULAR: type = config.getString("infractions.warning-type"); break;
            case FLOOD: type = config.getString("flood.warning-type"); break;
            default: return TypeUtils.WarningType.MESSAGE;
        }

        if(type.contains("title")){
            return TypeUtils.WarningType.TITLE;
        } else if(type.contains("actionbar")){
            return TypeUtils.WarningType.ACTIONBAR;
        } else if(type.contains("message")){
            return TypeUtils.WarningType.MESSAGE;
        } else {
            return TypeUtils.WarningType.MESSAGE;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type){
        String message = config.getString("spam.messages.warning");
        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        MiniMessage.miniMessage().parse(
                            message,
                            PlaceholderUtils.getTemplates(infractor))));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            MiniMessage.miniMessage().parse(
                                titleParts[0],
                                PlaceholderUtils.getTemplates(infractor)),
                            MiniMessage.miniMessage().parse(
                                titleParts[1],
                                PlaceholderUtils.getTemplates(infractor))));
                }
                break;
            case MESSAGE: player.sendMessage(MiniMessage.miniMessage().parse(message, PlaceholderUtils.getTemplates(infractor))); break;
            case ACTIONBAR: player.sendActionBar(MiniMessage.miniMessage().parse(message, PlaceholderUtils.getTemplates(infractor))); break;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param fUtils the flood check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, FloodCheck fUtils){
        String message = config.getString("flood.messages.warning");
        List<Template> template = new ArrayList<>();
        template.add(Template.of("infraction", fUtils.getInfractionWord()));
        template.addAll(PlaceholderUtils.getTemplates(infractor));

        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        MiniMessage.miniMessage().parse(
                            message,
                            template)));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            MiniMessage.miniMessage().parse(
                                titleParts[0],
                                template),
                            MiniMessage.miniMessage().parse(
                                titleParts[1],
                                template)));
                }
                break;
            case MESSAGE: player.sendMessage(MiniMessage.miniMessage().parse(message, template)); break;
            case ACTIONBAR: player.sendActionBar(MiniMessage.miniMessage().parse(message, template)); break;
        }
    }

    /**
     * Send a message of some kind to the offender.
     * @param infractor offender
     * @param type the type of infraction
     * @param iUtils the infractions check
     */
    public void sendWarningMessage(InfractionPlayer infractor, TypeUtils.InfractionType type, InfractionCheck iUtils){
        String message = config.getString("infractions.messages.warning");
        List<Template> template = new ArrayList<>();
        template.add(Template.of("infraction", iUtils.getInfractionWord()));
        template.addAll(PlaceholderUtils.getTemplates(infractor));

        Audience player = infractor.getPlayer().get();

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    player.showTitle(
                    Title.title(
                        Component.empty(),
                        MiniMessage.miniMessage().parse(
                            message,
                            template)));
                } else {
                    String titleParts[] = message.split(";");
                    player.showTitle(
                        Title.title(
                            MiniMessage.miniMessage().parse(
                                titleParts[0],
                                template),
                            MiniMessage.miniMessage().parse(
                                titleParts[1],
                                template)));
                }
                break;
            case MESSAGE: player.sendMessage(MiniMessage.miniMessage().parse(message, template)); break;
            case ACTIONBAR: player.sendActionBar(MiniMessage.miniMessage().parse(message, template)); break;
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param staff audience that has the required permission to receive the alert
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public void sendAlertMessage(Audience staff, InfractionPlayer infractor, TypeUtils.InfractionType type){
        String message;
        switch(type){
            case FLOOD: message = config.getString("flood.messages.alert"); break;
            case REGULAR: message = config.getString("infractions.messages.alert"); break;
            case SPAM: message = config.getString("spam.messages.alert"); break;
            default: message = null;
        }

        staff.sendMessage(
            MiniMessage.miniMessage().parse(
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
        MiniMessage mm = MiniMessage.miniMessage();
        switch(type){
            case REGULAR: sender.sendMessage(mm.parse(config.getString("infractions.messages.reset"), PlaceholderUtils.getTemplates(player))); break;
            case FLOOD: sender.sendMessage(mm.parse(config.getString("flood.messages.reset"), PlaceholderUtils.getTemplates(player))); break;
            case SPAM: sender.sendMessage(mm.parse(config.getString("spam.messages.reset"), PlaceholderUtils.getTemplates(player))); break;
            case NONE: sender.sendMessage(mm.parse(config.getString("general.messages.all-reset"), PlaceholderUtils.getTemplates(player))); break;
        }
    }
}
