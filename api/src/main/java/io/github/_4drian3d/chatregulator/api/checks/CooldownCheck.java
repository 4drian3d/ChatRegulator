package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class CooldownCheck implements Check {
    private final TimeUnit unit;
    private final long limit;
    private final SourceType sourceType;

    private CooldownCheck(TimeUnit unit, long limit, SourceType sourceType) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements AbstractBuilder<CooldownCheck> {
        private TimeUnit unit;
        private long limit;
        private SourceType source;

        @Required
        public Builder timeUnit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder limit(long limit) {
            this.limit = limit;
            return this;
        }

        @Required
        public Builder source(SourceType source) {
            this.source = source;
            return this;
        }

        @Override
        public @NotNull CooldownCheck build() {
            return new CooldownCheck(unit, limit, source);
        }
    }
}
