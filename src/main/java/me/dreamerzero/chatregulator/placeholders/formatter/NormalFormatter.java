package me.dreamerzero.chatregulator.placeholders.formatter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class NormalFormatter implements IFormatter {

    @Override
    public Component parse(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public Component parse(String string, Audience audience) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public Component parse(String string, Audience audience, TagResolver extraResolver) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }

    @Override
    public Component parse(String string, TagResolver extraResolver) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }
    
}
