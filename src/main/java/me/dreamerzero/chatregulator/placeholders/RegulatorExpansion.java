package me.dreamerzero.chatregulator.placeholders;

import java.util.Locale;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.miniplaceholders.api.Expansion;
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;

public final class RegulatorExpansion {
    private RegulatorExpansion(){}
    public static Expansion getExpansion(){
        return Expansion.builder("chatregulator")
            .filter(Player.class)
            .audiencePlaceholder("infractions_count", (aud, queue, ctx) -> {
                InfractionPlayer player = InfractionPlayer.get((Player)aud);
                String type = queue.popOr(() -> "you need to introduce the infraction type").value();
                return Tag.selfClosingInserting(
                    Component.text(
                        player.getViolations().getCount(
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
                TagsUtils.staticTag(InfractionPlayer.get((Player)aud).preLastCommand()))
            .audiencePlaceholder("last_command", (aud, queue, ctx) ->
                TagsUtils.staticTag(InfractionPlayer.get((Player)aud).lastCommand()))
            .audiencePlaceholder("pre_last_message", (aud, queue, ctx) ->
                TagsUtils.staticTag(InfractionPlayer.get((Player)aud).preLastMessage()))
            .audiencePlaceholder("last_message", (aud, queue, ctx) ->
                TagsUtils.staticTag(InfractionPlayer.get((Player)aud).lastMessage()))
            .build();
    }
}
