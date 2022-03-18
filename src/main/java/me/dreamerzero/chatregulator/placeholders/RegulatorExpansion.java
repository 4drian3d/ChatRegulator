package me.dreamerzero.chatregulator.placeholders;

import java.util.Locale;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.miniplaceholders.api.Expansion;
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
                final int count;
                switch(type.toLowerCase(Locale.ROOT)){
                    case "flood": count = player.getViolations().getCount(InfractionType.FLOOD); break;
                    case "spam": count = player.getViolations().getCount(InfractionType.SPAM); break;
                    case "regular": count = player.getViolations().getCount(InfractionType.REGULAR); break;
                    case "command": count = player.getViolations().getCount(InfractionType.BCOMMAND); break;
                    case "unicode": count = player.getViolations().getCount(InfractionType.UNICODE); break;
                    case "syntax": count = player.getViolations().getCount(InfractionType.SYNTAX); break;
                    default: count = 0;
                }
                return Tag.selfClosingInserting(Component.text(count));
            })
            .audiencePlaceholder("pre_last_command", (aud, queue, ctx) ->
                Tag.selfClosingInserting(Component.text(InfractionPlayer.get((Player)aud).preLastCommand())))
            .audiencePlaceholder("last_command", (aud, queue, ctx) ->
                Tag.selfClosingInserting(Component.text(InfractionPlayer.get((Player)aud).lastCommand())))
            .audiencePlaceholder("pre_last_message", (aud, queue, ctx) -> 
                Tag.selfClosingInserting(Component.text(InfractionPlayer.get((Player)aud).preLastMessage())))
            .audiencePlaceholder("last_message", (aud, queue, ctx) -> 
                Tag.selfClosingInserting(Component.text(InfractionPlayer.get((Player)aud).lastMessage())))
            .build();
    }
}
