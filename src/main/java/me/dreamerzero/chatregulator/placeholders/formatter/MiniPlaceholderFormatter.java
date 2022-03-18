package me.dreamerzero.chatregulator.placeholders.formatter;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniPlaceholderFormatter implements IFormatter {

    @Override
    public Component parse(String string) {
        TagResolver global = MiniPlaceholders.getGlobalPlaceholders();
        return MiniMessage.miniMessage().deserialize(string, global);
    }

    @Override
    public Component parse(String string, Player player) {
        TagResolver resolver = TagResolver.resolver(
            MiniPlaceholders.getAudiencePlaceholders(player),
            MiniPlaceholders.getGlobalPlaceholders());
        return MiniMessage.miniMessage().deserialize(string, resolver);
    }

    @Override
    public Component parse(String string, Player player, TagResolver extraResolver) {
        TagResolver resolver = TagResolver.resolver(
            MiniPlaceholders.getGlobalPlaceholders(),
            MiniPlaceholders.getAudiencePlaceholders(player),
            extraResolver);
        return MiniMessage.miniMessage().deserialize(string, resolver);
    }

    @Override
    public Component parse(String string, TagResolver extraResolver) {
        TagResolver global = TagResolver.resolver(MiniPlaceholders.getGlobalPlaceholders(), extraResolver);
        return MiniMessage.miniMessage().deserialize(string, global);
    }
    
}
