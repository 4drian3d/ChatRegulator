package io.github._4drian3d.chatregulator.plugin;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import io.github._4drian3d.chatregulator.api.Statistics;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;

import static io.github._4drian3d.chatregulator.plugin.utils.Placeholders.integer;

public final class StatisticsImpl implements Statistics {
    private final EnumMap<InfractionType, Integer> countMap = new EnumMap<>(InfractionType.class);

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addInfractionCount(@NotNull InfractionType type) {
        Preconditions.checkArgument(type != InfractionType.GLOBAL);

        countMap.merge(type, 1, Integer::sum);
        countMap.merge(InfractionType.GLOBAL, 1, Integer::sum);
    }

    /**
     * Obtain the number of infractions of some type
     * @param type the infraction type
     * @return count of the respective infraction type
     */
    @Override
    public int getInfractionCount(@NotNull InfractionType type){
        return countMap.get(type);
    }

    public TagResolver getPlaceholders() {
        TagResolver.Builder builder = TagResolver.builder();
        countMap.forEach(((infractionType, integer) -> builder.resolver(integer(infractionType.toString().toLowerCase(Locale.ROOT), integer == null ? 0 : integer))));
        return builder.build();
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
        return builder.append("]").toString();
    }
}
