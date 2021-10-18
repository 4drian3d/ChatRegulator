package net.dreamerzero.chatregulator.utils;

import java.util.List;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.events.ViolationEvent;
import net.kyori.adventure.text.minimessage.Template;

/**
 * Player Data Collection Utilities
 */
public class PlaceholderUtils {
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public static List<Template> getTemplates(final InfractionPlayer player){
        return List.of(
            Template.of("player", player.username()),
            Template.of("server", player.isOnline() ? player.getPlayer().get().getCurrentServer().get().getServerInfo().getName() : "Offline Player"),
            Template.of("flood", String.valueOf(player.getFloodInfractions())),
            Template.of("spam", String.valueOf(player.getSpamInfractions())),
            Template.of("regular", String.valueOf(player.getRegularInfractions())));
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public static List<Template> getGlobalTemplates(){
        return List.of(
            Template.of("flood", String.valueOf(ViolationEvent.floodCount)),
            Template.of("spam", String.valueOf(ViolationEvent.spamCount)),
            Template.of("regular", String.valueOf(ViolationEvent.regularCount)),
            Template.of("command", String.valueOf(ViolationEvent.commandCount))
        );
    }
}
