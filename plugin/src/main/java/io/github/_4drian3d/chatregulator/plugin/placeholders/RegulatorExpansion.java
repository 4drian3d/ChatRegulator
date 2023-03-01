package io.github._4drian3d.chatregulator.plugin.placeholders;

import java.util.Locale;

import com.velocitypowered.api.proxy.Player;

import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

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
                                case "regular" -> InfractionType.REGULAR;
                                case "command" -> InfractionType.BCOMMAND;
                                case "unicode" -> InfractionType.UNICODE;
                                case "syntax" -> InfractionType.SYNTAX;
                                default -> InfractionType.NONE;
                            }
                        )
                    )
                );
            })
            .audiencePlaceholder("pre_last_command", (aud, queue, ctx) ->
                TagsUtils.staticTag(manager.getPlayer((Player)aud).preLastCommand()))
            .audiencePlaceholder("last_command", (aud, queue, ctx) ->
                TagsUtils.staticTag(manager.getPlayer((Player)aud).lastCommand()))
            .audiencePlaceholder("pre_last_message", (aud, queue, ctx) ->
                TagsUtils.staticTag(manager.getPlayer((Player)aud).preLastMessage()))
            .audiencePlaceholder("last_message", (aud, queue, ctx) ->
                TagsUtils.staticTag(manager.getPlayer((Player)aud).lastMessage()))
            .build();
    }
}
