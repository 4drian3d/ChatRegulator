package me.dreamerzero.chatregulator.placeholders.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class MiniPlaceholderFormatter implements IFormatter {

    @Override
    public Component parse(
        final @NotNull String string
    ) {
        return MiniMessage.miniMessage().deserialize(
            string,
            MiniPlaceholders.getGlobalPlaceholders()
        );
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience
    ) {
        if(audience == null){
            return parse(string);
        }
        return MiniMessage.miniMessage().deserialize(
            string,
            MiniPlaceholders.getAudienceGlobalPlaceholders(audience)
        );
    }

    @Override
    public Component parse(
        final @NotNull String string,
        final @Nullable Audience audience,
        final @NotNull TagResolver extraResolver
    ) {
        if(audience == null){
            return parse(string, extraResolver);
        }
        return MiniMessage.miniMessage().deserialize(
            string,
            MiniPlaceholders.getAudienceGlobalPlaceholders(audience),
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
            MiniPlaceholders.getGlobalPlaceholders(),
            extraResolver
        );
    }

    @Override
    public TagResolver resolver(Audience audience) {
        return MiniPlaceholders.getAudienceGlobalPlaceholders(audience);
    }

    @Override
    public TagResolver resolver() {
        return MiniPlaceholders.getGlobalPlaceholders();
    }
}
