package me.dreamerzero.chatregulator.placeholders.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class NormalFormatter implements IFormatter {

    @Override
    public Component parse(
        final @NotNull String string
    ) {
        return MiniMessage.miniMessage().deserialize(string);
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience
    ) {
        return this.parse(string);
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience,
        final @NotNull TagResolver extraResolver
    ) {
        return this.parse(string, extraResolver);
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @NotNull TagResolver extraResolver
    ) {
        return MiniMessage.miniMessage().deserialize(string, extraResolver);
    }

    @Override
    public TagResolver resolver(Audience audience) {
        return TagResolver.empty();
    }

    @Override
    public TagResolver resolver() {
        return TagResolver.empty();
    }
}
