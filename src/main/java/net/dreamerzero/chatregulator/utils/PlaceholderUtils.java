package net.dreamerzero.chatregulator.utils;

import java.util.List;

import net.dreamerzero.chatregulator.InfractionPlayer;
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
    public static List<Template> getTemplates(InfractionPlayer player){
        return List.of(
            Template.of("player", player.username()),
            Template.of("server", player.getPlayer().getCurrentServer().get().getServerInfo().getName()),
            Template.of("flood", String.valueOf(player.getFloodInfractions())),
            Template.of("spam", String.valueOf(player.getSpamInfractions())),
            Template.of("regular", String.valueOf(player.getRegularInfractions())));
    }
}
