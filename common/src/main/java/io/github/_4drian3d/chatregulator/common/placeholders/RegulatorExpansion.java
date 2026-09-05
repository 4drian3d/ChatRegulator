package io.github._4drian3d.chatregulator.common.placeholders;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.PlayerManager;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.tag.Tag;

import java.util.Locale;
import java.util.UUID;

public final class RegulatorExpansion {
    private RegulatorExpansion() {}
    public static Expansion getExpansion(final PlayerManager manager) {
        return Expansion.builder("chatregulator")
            .audiencePlaceholder("infractions_count", (p, queue, ctx) -> {
                final UUID uuid = p.getOrDefault(Identity.UUID, null);
                if (uuid == null) {
                    return null;
                }
                final InfractionPlayer player = manager.getPlayer(uuid);
                final String type = queue.popOr("you need to introduce the infraction type")
                        .value().toUpperCase(Locale.ROOT);
                final InfractionType infractionType = InfractionType.INDEX.valueOrThrow(type);

                return Tag.preProcessParsed(Integer.toString(player.getInfractions().getCount(infractionType)));
            })
            .build();
    }
}
