package me.dreamerzero.chatregulator.placeholders.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public interface IFormatter {
    Component parse(
        final @NotNull String string
    );

    Component parse(
        final @NotNull String string,
        final @NotNull TagResolver extraResolver
    );

    Component parse(
        final @NotNull String string,
        final @Nullable Audience audience
    );

    Component parse(
        final @NotNull String string,
        final @Nullable Audience audience,
        final @NotNull TagResolver extraResolver
    );
}
