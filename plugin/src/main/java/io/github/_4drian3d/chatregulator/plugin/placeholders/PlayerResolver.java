package io.github._4drian3d.chatregulator.plugin.placeholders;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class PlayerResolver implements TagResolver {
    private final InfractionPlayer player;

    public PlayerResolver(final InfractionPlayer player) {
        this.player = player;
    }

    @Override
    public @Nullable Tag resolve(
            final @NotNull String name,
            final @NotNull ArgumentQueue arguments,
            final @NotNull Context ctx
    ) throws ParsingException {
        if (name.equalsIgnoreCase("player") || name.equalsIgnoreCase("name")) {
            return Tag.preProcessParsed(player.username());
        }
        final InfractionType type = InfractionType.INDEX.value(name.toUpperCase(Locale.ROOT));
        if (type == null) {
            return null;
        }
        final int count = player.getInfractions().getCount(type);
        return Tag.selfClosingInserting(Component.text(count));
    }

    @Override
    public boolean has(final @NotNull String name) {
        if (name.equalsIgnoreCase("player") || name.equalsIgnoreCase("name")) {
            return true;
        }
        return InfractionType.INDEX.value(name.toUpperCase(Locale.ROOT)) != null;
    }
}
