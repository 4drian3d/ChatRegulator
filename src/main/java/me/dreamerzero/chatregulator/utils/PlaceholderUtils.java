package me.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

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
    public static PlaceholderResolver getPlaceholders(@NotNull final InfractionPlayer player){
        ViolationCount count = player.getViolations();
        Set<Placeholder<String>> placeholders = Set.of(
            Placeholder.miniMessage("player", player.username()),
            Placeholder.miniMessage("name", player.username()),
            Placeholder.miniMessage("flood", String.valueOf(count.getCount(InfractionType.FLOOD))),
            Placeholder.miniMessage("spam", String.valueOf(count.getCount(InfractionType.SPAM))),
            Placeholder.miniMessage("regular", String.valueOf(count.getCount(InfractionType.REGULAR))),
            Placeholder.miniMessage("unicode", String.valueOf(count.getCount(InfractionType.UNICODE))),
            Placeholder.miniMessage("caps", String.valueOf(count.getCount(InfractionType.CAPS))),
            Placeholder.miniMessage("command", String.valueOf(count.getCount(InfractionType.BCOMMAND)))
        );
        Player p = player.getPlayer();
        if(p != null){
            p.getCurrentServer().ifPresent(server ->
                placeholders.add(
                    Placeholder.miniMessage("server", server.getServer().getServerInfo().getName())));
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
            Placeholder.miniMessage("flood", String.valueOf(statistics.getViolationCount(InfractionType.FLOOD))),
            Placeholder.miniMessage("spam", String.valueOf(statistics.getViolationCount(InfractionType.SPAM))),
            Placeholder.miniMessage("regular", String.valueOf(statistics.getViolationCount(InfractionType.REGULAR))),
            Placeholder.miniMessage("command", String.valueOf(statistics.getViolationCount(InfractionType.BCOMMAND))),
            Placeholder.miniMessage("unicode", String.valueOf(statistics.getViolationCount(InfractionType.UNICODE))),
            Placeholder.miniMessage("caps", String.valueOf(statistics.getViolationCount(InfractionType.CAPS)))
        );
    }

    private PlaceholderUtils(){}
}
