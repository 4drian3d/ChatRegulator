package me.dreamerzero.chatregulator.placeholders.formatter;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface IFormatter {
    Component parse(String string);

    Component parse(String string, TagResolver extraResolver);

    Component parse(String string, Player player);

    Component parse(String string, Player player, TagResolver extraResolver);
}
