package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Check that enforces a cooldown timeout between messages or commands from a player.
 */
public final class CooldownCheck implements Check {
    private final TimeUnit unit;
    private final long limit;
    private final SourceType sourceType;

    private CooldownCheck(final TimeUnit unit, final long limit, final SourceType sourceType) {
        this.limit = limit;
        this.unit = unit;
        this.sourceType = sourceType;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final Instant lastExecuted = player.getChain(sourceType).lastExecuted();
        if (Duration.between(lastExecuted, Instant.now()).toMillis() < unit.toMillis(limit)) {
            return CheckResult.denied(type());
        }
        return CheckResult.allowed();
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.COOLDOWN;
    }

    /**
     * Creates a new Builder
     *
     * @return a new CooldownCheck Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Cooldown Check Builder
     */
    public static class Builder implements AbstractBuilder<CooldownCheck> {
        private TimeUnit unit;
        private long limit;
        private SourceType source;

        private Builder() {}

        /**
         * Set the time unit for the cooldown duration.
         * 
         * @param unit the time unit (SECONDS, MILLISECONDS, etc.)
         * @return this builder for chaining
         * @throws NullPointerException if unit is null
         */
        @Required
        public Builder timeUnit(final @NotNull TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Set the cooldown limit in the specified time unit.
         * 
         * @param limit the cooldown duration (non-negative)
         * @return this builder for chaining
         */
        public Builder limit(final @NonNegative long limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Set whether this cooldown applies to messages or commands.
         * 
         * @param source the source type
         * @return this builder for chaining
         * @throws NullPointerException if source is null
         */
        @Required
        public Builder source(final @NotNull SourceType source) {
            this.source = source;
            return this;
        }

        @Override
        public @NotNull CooldownCheck build() {
            return new CooldownCheck(unit, limit, source);
        }
    }
}
