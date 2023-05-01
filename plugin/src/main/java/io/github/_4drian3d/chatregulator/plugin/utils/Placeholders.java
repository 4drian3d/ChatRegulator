package io.github._4drian3d.chatregulator.plugin.utils;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class Placeholders {
    public static TagResolver.Single integer(String key, int value) {
        return Placeholder.parsed(key, Integer.toString(value));
    }
}
