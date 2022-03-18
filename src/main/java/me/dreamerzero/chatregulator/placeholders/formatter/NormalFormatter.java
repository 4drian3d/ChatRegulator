package me.dreamerzero.chatregulator.placeholders.formatter;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class NormalFormatter implements IFormatter {

    @Override
    public Component parse(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public Component parse(String string, Player player) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public Component parse(String string, Player player, TagResolver extraResolver) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }

    @Override
    public Component parse(String string, TagResolver extraResolver) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }
    
}
