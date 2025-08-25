package io.github._4drian3d.chatregulator.plugin.placeholders;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.PlayerManager;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.minimessage.tag.Tag;

import java.util.Locale;

public final class RegulatorExpansion {
    private RegulatorExpansion(){}
    public static Expansion getExpansion(final PlayerManager manager) {
        return Expansion.builder("chatregulator")
            .audiencePlaceholder(Player.class, "infractions_count", (p, queue, ctx) -> {
                final InfractionPlayer player = manager.getPlayer(p.getUniqueId());
                final String type = queue.popOr("you need to introduce the infraction type")
                        .value().toUpperCase(Locale.ROOT);
                final InfractionType infractionType = InfractionType.INDEX.valueOrThrow(type);

                return Tag.preProcessParsed(Integer.toString(player.getInfractions().getCount(infractionType)));
            })
            .build();
    }
}
