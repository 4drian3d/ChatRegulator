package net.dreamerzero.chatregulator.config;

import com.velocitypowered.api.proxy.Player;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;

public class ConfigManager {
    private Yaml config;
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
    public void sendWarningMessage(Audience infractor, TypeUtils.InfractionType type){
        String message;
        switch(type){
            case FLOOD: message = config.getString("flood.messages.warning");
            case REGULAR: message = config.getString("infractions.messages.warning");
            case SPAM: message = config.getString("spam.messages.warning");
            default: message = "";
        }

        switch(getWarningType(type)){
            case TITLE:
                if(!message.contains(";")){
                    infractor.showTitle(
                    Title.title(
                        Component.empty(),
                        MiniMessage.miniMessage().parse(
                            message)));
                } else {
                    String titleParts[] = message.split(";");
                    infractor.showTitle(
                        Title.title(
                            MiniMessage.miniMessage().parse(
                                titleParts[0]),
                            MiniMessage.miniMessage().parse(
                                titleParts[1])));
                }
                break;
            case MESSAGE: infractor.sendMessage(MiniMessage.miniMessage().parse(message)); break;
            case ACTIONBAR: infractor.sendActionBar(MiniMessage.miniMessage().parse(message)); break;
        }
    }

    /**
     * Sends an alert message to users who are in the audience with the required permissions
     * @param staff audience that has the required permission to receive the alert
     * @param infractor the player who committed the infraction
     * @param type the type of infraction
     */
    public void sendAlertMessage(Audience staff, Player infractor, TypeUtils.InfractionType type){
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
                new PlaceholderUtils().getTemplates(infractor)));
    }

    public void sendResetMessage(Audience sender, TypeUtils.InfractionType type, InfractionPlayer player){
        MiniMessage mm = MiniMessage.miniMessage();
        switch(type){
            case REGULAR: sender.sendMessage(mm.parse(config.getString("infractions.messages.reset"), "player", player.username())); break;
            case FLOOD: sender.sendMessage(mm.parse(config.getString("flood.messages.reset"), "player", player.username())); break;
            case SPAM: sender.sendMessage(mm.parse(config.getString("spam.messages.reset"), "player", player.username())); break;
            case NONE: sender.sendMessage(mm.parse(config.getString("general.messages.all-reset"), "player", player.username())); break;
        }
    }
}
