package me.dreamerzero.chatregulator.placeholders.formatter;

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class MiniPlaceholderFormatter implements IFormatter {

    @Override
    public Component parse(String string) {
        TagResolver global = MiniPlaceholders.getGlobalPlaceholders();
        return MiniMessage.miniMessage().deserialize(string, global);
    }

    @Override
    public Component parse(String string, Audience audience) {
        if(audience == null){
            return parse(string);
        }
        TagResolver resolver = TagResolver.resolver(
            MiniPlaceholders.getAudiencePlaceholders(audience),
            MiniPlaceholders.getGlobalPlaceholders());
        return MiniMessage.miniMessage().deserialize(string, resolver);
    }

    @Override
    public Component parse(String string, Audience audience, TagResolver extraResolver) {
        if(audience == null){
            return parse(string, extraResolver);
        }
        TagResolver resolver = TagResolver.resolver(
            MiniPlaceholders.getGlobalPlaceholders(),
            MiniPlaceholders.getAudiencePlaceholders(audience),
            extraResolver);
        return MiniMessage.miniMessage().deserialize(string, resolver);
    }

    @Override
    public Component parse(String string, TagResolver extraResolver) {
        TagResolver global = TagResolver.resolver(MiniPlaceholders.getGlobalPlaceholders(), extraResolver);
        return MiniMessage.miniMessage().deserialize(string, global);
    }
    
}
