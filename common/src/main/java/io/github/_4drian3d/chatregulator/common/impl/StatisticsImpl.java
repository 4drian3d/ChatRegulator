package io.github._4drian3d.chatregulator.common.impl;

import io.github._4drian3d.chatregulator.api.Statistics;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Objects;

public final class StatisticsImpl implements Statistics, TagResolver {
    private final EnumMap<InfractionType, Integer> countMap = new EnumMap<>(InfractionType.class);

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addInfractionCount(@NotNull InfractionType type) {
        if (type == InfractionType.GLOBAL) {
            throw new IllegalArgumentException("Invalid InfractionType provided");
        }

        countMap.merge(type, 1, Integer::sum);
    }

    @Override
    public int getInfractionCount(@NotNull InfractionType type) {
        if (type == InfractionType.GLOBAL) {
            int count = 0;
            for (final int infraction : countMap.values()) {
                count += infraction;
            }
            return count;
        }
        return countMap.get(type);
    }

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        final InfractionType type = InfractionType.INDEX.value(name.toUpperCase(Locale.ROOT));
        if (type == null) {
            return null;
        }
        final Integer count = countMap.get(type);
        return Tag.selfClosingInserting(Component.text(count == null ? 0 : count));
    }

    @Override
    public boolean has(final @NotNull String name) {
        final InfractionType type = InfractionType.INDEX.value(name.toUpperCase(Locale.ROOT));
        if (type == null) {
            return false;
        }
        return countMap.get(type) != null;
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof final StatisticsImpl that)) {
            return false;
        }

        return Objects.equals(this.countMap, that.countMap);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.countMap);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder("StatisticsImpl[");
        countMap.forEach(((infractionType, integer) -> builder.append(infractionType).append('=').append(integer)));
        return builder.append(']').toString();
    }
}
