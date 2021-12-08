package me.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.enums.InfractionType;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

/**
 * Player Data Collection Utilities
 */
public class PlaceholderUtils {
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public static PlaceholderResolver getPlaceholders(final InfractionPlayer player){
        Set<Placeholder> placeholders = Set.of(
            Placeholder.placeholder("player", player.username()),
            Placeholder.placeholder("name", player.username()),
            Placeholder.placeholder("flood", String.valueOf(player.getViolations().getCount(InfractionType.FLOOD))),
            Placeholder.placeholder("spam", String.valueOf(player.getViolations().getCount(InfractionType.SPAM))),
            Placeholder.placeholder("regular", String.valueOf(player.getViolations().getCount(InfractionType.REGULAR))),
            Placeholder.placeholder("unicode", String.valueOf(player.getViolations().getCount(InfractionType.UNICODE))),
            Placeholder.placeholder("caps", String.valueOf(player.getViolations().getCount(InfractionType.CAPS)))
        );
        Player p = player.getPlayer();
        if(p != null){
            p.getCurrentServer().ifPresent(server ->
                placeholders.add(Placeholder.placeholder("server", server.getServer().getServerInfo().getName())));
        }

        return PlaceholderResolver.placeholders(placeholders);
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public static PlaceholderResolver getGlobalPlaceholders(){
        return PlaceholderResolver.placeholders(
            Placeholder.placeholder("flood", String.valueOf(Statistics.getViolationCount(InfractionType.FLOOD))),
            Placeholder.placeholder("spam", String.valueOf(Statistics.getViolationCount(InfractionType.SPAM))),
            Placeholder.placeholder("regular", String.valueOf(Statistics.getViolationCount(InfractionType.REGULAR))),
            Placeholder.placeholder("command", String.valueOf(Statistics.getViolationCount(InfractionType.BCOMMAND))),
            Placeholder.placeholder("unicode", String.valueOf(Statistics.getViolationCount(InfractionType.UNICODE))),
            Placeholder.placeholder("caps", String.valueOf(Statistics.getViolationCount(InfractionType.CAPS)))
        );
    }

    private PlaceholderUtils(){}
}
