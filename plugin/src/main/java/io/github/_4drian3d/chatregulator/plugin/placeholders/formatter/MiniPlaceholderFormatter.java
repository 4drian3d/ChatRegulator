package io.github._4drian3d.chatregulator.plugin.placeholders.formatter;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MiniPlaceholderFormatter extends Formatter {

    @Override
    public Component parse(
        final @NotNull String string
    ) {
        return MiniMessage.miniMessage().deserialize(
            string,
            MiniPlaceholders.globalPlaceholders()
        );
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience
    ) {
        if (audience == null){
            return parse(string);
        }
        return MiniMessage.miniMessage().deserialize(
            string,
            audience,
            MiniPlaceholders.audienceGlobalPlaceholders()
        );
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience,
        final @NotNull TagResolver extraResolver
    ) {
        if (audience == null){
            return parse(string, extraResolver);
        }
        return MiniMessage.miniMessage().deserialize(
            string,
            audience,
            MiniPlaceholders.audienceGlobalPlaceholders(),
            extraResolver
        );
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @NotNull TagResolver extraResolver
    ) {
        return MiniMessage.miniMessage().deserialize(
            string,
            MiniPlaceholders.globalPlaceholders(),
            extraResolver
        );
    }

    @Override
    public TagResolver resolver(Audience audience) {
        return MiniPlaceholders.audienceGlobalPlaceholders();
    }

    @Override
    public TagResolver resolver() {
        return MiniPlaceholders.globalPlaceholders();
    }
}
