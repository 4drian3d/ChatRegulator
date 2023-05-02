package io.github._4drian3d.chatregulator.plugin.placeholders;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

import java.util.Locale;

public final class RegulatorExpansion {
    private RegulatorExpansion(){}
    public static Expansion getExpansion(PlayerManagerImpl manager){
        return Expansion.builder("chatregulator")
            .filter(Player.class)
            .audiencePlaceholder("infractions_count", (aud, queue, ctx) -> {
                InfractionPlayerImpl player = manager.getPlayer((Player)aud);
                String type = queue.popOr(() -> "you need to introduce the infraction type").value();
                return Tag.selfClosingInserting(
                    Component.text(
                        player.getInfractions().getCount(
                            switch(type.toLowerCase(Locale.ROOT)){
                                case "flood" -> InfractionType.FLOOD;
                                case "spam" -> InfractionType.SPAM;
                                case "regular" -> InfractionType.REGEX;
                                case "command" -> InfractionType.BLOCKED_COMMAND;
                                case "unicode" -> InfractionType.UNICODE;
                                case "syntax" -> InfractionType.SYNTAX;
                                case "global" -> InfractionType.GLOBAL;
                                default -> throw ctx.newException("Invalid Infraction Type");
                            }
                        )
                    )
                );
            })
            .build();
    }
}
