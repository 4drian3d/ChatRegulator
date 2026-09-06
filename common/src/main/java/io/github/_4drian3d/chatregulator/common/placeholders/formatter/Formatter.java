package io.github._4drian3d.chatregulator.common.placeholders.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public sealed class Formatter permits MiniPlaceholderFormatter {

    public Component parse(
        final @NotNull String string
    ) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience
    ) {
        return this.parse(string);
    }

    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience,
        final @NotNull TagResolver extraResolver
    ) {
        return this.parse(string, extraResolver);
    }

    public Component parse(
        final @NotNull String string,
        final @NotNull TagResolver extraResolver
    ) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }

    public TagResolver resolver(Audience audience) {
        return TagResolver.empty();
    }

    public TagResolver resolver() {
        return TagResolver.empty();
    }
}
