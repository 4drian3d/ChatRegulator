package me.dreamerzero.chatregulator.utils;

import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ViolationCount;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.enums.InfractionType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Player Data Collection Utilities
 * 
 * This class returns placeholders whether or not MiniPlaceholders is installed
 */
public final class Placeholders {
    private final ChatRegulator plugin;

    public Placeholders(ChatRegulator plugin) {
        this.plugin = plugin;
    }
    /**
     * Obtain placeholders from an {@link InfractionPlayer}
     * @param player the {@link InfractionPlayer}
     * @return placeholders based on this player
     */
    public @NotNull TagResolver getPlaceholders(final @NotNull InfractionPlayer player){
        final ViolationCount count = Objects.requireNonNull(player).getViolations();
        final TagResolver.Builder resolver = TagResolver.builder().resolvers(
            Placeholder.unparsed("player", player.username()),
            Placeholder.unparsed("name", player.username()),
            integerPlaceholder("flood", count.getCount(InfractionType.FLOOD)),
            integerPlaceholder("spam", count.getCount(InfractionType.SPAM)),
            integerPlaceholder("regular", count.getCount(InfractionType.REGULAR)),
            integerPlaceholder("unicode", count.getCount(InfractionType.UNICODE)),
            integerPlaceholder("caps", count.getCount(InfractionType.CAPS)),
            integerPlaceholder("command", count.getCount(InfractionType.BCOMMAND)),
            integerPlaceholder("syntax", count.getCount(InfractionType.SYNTAX))
        );
        Player p = player.getPlayer();
        if(p != null){
            p.getCurrentServer().ifPresent(server ->
                resolver.resolver(Placeholder.unparsed("server", server.getServer().getServerInfo().getName())));
        }

        return resolver.build();
    }

    /**
     * Obtain the global placeholders
     * @return global placeholders
     */
    public @NotNull TagResolver getGlobalPlaceholders(){
        final Statistics statistics = plugin.getStatistics();
        return TagResolver.resolver(
            integerPlaceholder("flood", statistics.getViolationCount(InfractionType.FLOOD)),
            integerPlaceholder("spam", statistics.getViolationCount(InfractionType.SPAM)),
            integerPlaceholder("regular", statistics.getViolationCount(InfractionType.REGULAR)),
            integerPlaceholder("command", statistics.getViolationCount(InfractionType.BCOMMAND)),
            integerPlaceholder("unicode", statistics.getViolationCount(InfractionType.UNICODE)),
            integerPlaceholder("caps", statistics.getViolationCount(InfractionType.CAPS)),
            integerPlaceholder("syntax", statistics.getViolationCount(InfractionType.SYNTAX))
        );
    }

    private static TagResolver.Single integerPlaceholder(String key, int value){
        return Placeholder.unparsed(key, Integer.toString(value));
    }
}
