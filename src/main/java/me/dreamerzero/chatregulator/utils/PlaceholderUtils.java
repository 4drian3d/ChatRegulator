package me.dreamerzero.chatregulator.utils;

import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ViolationCount;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.enums.InfractionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

/**
 * Player Data Collection Utilities
 */
public final class PlaceholderUtils {
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public static @NotNull PlaceholderResolver getPlaceholders(@NotNull final InfractionPlayer player){
        final ViolationCount count = Objects.requireNonNull(player).getViolations();
        PlaceholderResolver.Builder resolver = PlaceholderResolver.builder();
        resolver.placeholders(
            Placeholder.raw("player", player.username()),
            Placeholder.raw("name", player.username()),
            stringPlaceholder("flood", count.getCount(InfractionType.FLOOD)),
            stringPlaceholder("spam", count.getCount(InfractionType.SPAM)),
            stringPlaceholder("regular", count.getCount(InfractionType.REGULAR)),
            stringPlaceholder("unicode", count.getCount(InfractionType.UNICODE)),
            stringPlaceholder("caps", count.getCount(InfractionType.CAPS)),
            stringPlaceholder("command", count.getCount(InfractionType.BCOMMAND))
        );
        Player p = player.getPlayer();
        if(p != null){
            p.getCurrentServer().ifPresent(server ->
                resolver.placeholder(
                    Placeholder.raw("server", server.getServer().getServerInfo().getName())));
        }

        return resolver.build();
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public static @NotNull PlaceholderResolver getGlobalPlaceholders(){
        Statistics statistics = Statistics.getStatistics();
        return PlaceholderResolver.placeholders(
            stringPlaceholder("flood", statistics.getViolationCount(InfractionType.FLOOD)),
            stringPlaceholder("spam", statistics.getViolationCount(InfractionType.SPAM)),
            stringPlaceholder("regular", statistics.getViolationCount(InfractionType.REGULAR)),
            stringPlaceholder("command", statistics.getViolationCount(InfractionType.BCOMMAND)),
            stringPlaceholder("unicode", statistics.getViolationCount(InfractionType.UNICODE)),
            stringPlaceholder("caps", statistics.getViolationCount(InfractionType.CAPS))
        );
    }

    private static Placeholder<Component> stringPlaceholder(String key, int value){
        return Placeholder.raw(key, String.valueOf(value));
    }

    private PlaceholderUtils(){}
}
