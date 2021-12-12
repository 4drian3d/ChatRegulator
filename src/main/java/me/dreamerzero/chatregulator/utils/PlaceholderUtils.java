package me.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ViolationCount;
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
        ViolationCount count = player.getViolations();
        Set<Placeholder> placeholders = Set.of(
            Placeholder.placeholder("player", player.username()),
            Placeholder.placeholder("name", player.username()),
            Placeholder.placeholder("flood", String.valueOf(count.getCount(InfractionType.FLOOD))),
            Placeholder.placeholder("spam", String.valueOf(count.getCount(InfractionType.SPAM))),
            Placeholder.placeholder("regular", String.valueOf(count.getCount(InfractionType.REGULAR))),
            Placeholder.placeholder("unicode", String.valueOf(count.getCount(InfractionType.UNICODE))),
            Placeholder.placeholder("caps", String.valueOf(count.getCount(InfractionType.CAPS))),
            Placeholder.placeholder("command", String.valueOf(count.getCount(InfractionType.BCOMMAND)))
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
        Statistics statistics = Statistics.getStatistics();
        return PlaceholderResolver.placeholders(
            Placeholder.placeholder("flood", String.valueOf(statistics.getViolationCount(InfractionType.FLOOD))),
            Placeholder.placeholder("spam", String.valueOf(statistics.getViolationCount(InfractionType.SPAM))),
            Placeholder.placeholder("regular", String.valueOf(statistics.getViolationCount(InfractionType.REGULAR))),
            Placeholder.placeholder("command", String.valueOf(statistics.getViolationCount(InfractionType.BCOMMAND))),
            Placeholder.placeholder("unicode", String.valueOf(statistics.getViolationCount(InfractionType.UNICODE))),
            Placeholder.placeholder("caps", String.valueOf(statistics.getViolationCount(InfractionType.CAPS)))
        );
    }

    private PlaceholderUtils(){}
}
