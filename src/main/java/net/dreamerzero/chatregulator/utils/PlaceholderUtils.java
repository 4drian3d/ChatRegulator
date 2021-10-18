package net.dreamerzero.chatregulator.utils;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.events.ViolationEvent;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;

/**
 * Player Data Collection Utilities
 */
public class PlaceholderUtils {
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public static TemplateResolver getTemplates(final InfractionPlayer player){
        return TemplateResolver.templates(
            Template.template("player", player.username()),
            Template.template("server", player.isOnline() ? player.getPlayer().get().getCurrentServer().get().getServerInfo().getName() : "Offline Player"),
            Template.template("flood", String.valueOf(player.getFloodInfractions())),
            Template.template("spam", String.valueOf(player.getSpamInfractions())),
            Template.template("regular", String.valueOf(player.getRegularInfractions())));
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public static TemplateResolver getGlobalTemplates(){
        return TemplateResolver.templates(
            Template.template("flood", String.valueOf(ViolationEvent.floodCount)),
            Template.template("spam", String.valueOf(ViolationEvent.spamCount)),
            Template.template("regular", String.valueOf(ViolationEvent.regularCount)),
            Template.template("command", String.valueOf(ViolationEvent.commandCount))
        );
    }
}
