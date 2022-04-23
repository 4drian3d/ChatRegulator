package me.dreamerzero.chatregulator.placeholders.formatter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface IFormatter {
    Component parse(String string);

    Component parse(String string, TagResolver extraResolver);

    Component parse(String string, Audience audience);

    Component parse(String string, Audience audience, TagResolver extraResolver);
}
