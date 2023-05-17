package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.StringChain;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Detection of command/message spamming
 */
public final class SpamCheck implements Check {
    private final SourceType type;
    private final int similarLimit;

    private SpamCheck(final @NotNull SourceType type, int similarLimit) {
        this.type = requireNonNull(type);
        this.similarLimit = similarLimit;
    }

    @Override
    public @NotNull CheckResult check(final @NotNull InfractionPlayer player, final @NotNull String string) {
        final StringChain chain = player.getChain(type);
        final int size = chain.size();
        if (size < similarLimit) {
            return CheckResult.allowed();
        }
        for (int i = 0; i + 1 < size; i++) {
            if (!chain.index(i).equalsIgnoreCase(chain.index(i + 1))) {
                return CheckResult.allowed();
            }
        }

        if (chain.last().equalsIgnoreCase(string)) {
            return CheckResult.denied(type());
        } else {
            return CheckResult.allowed();
        }
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SPAM;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder implements AbstractBuilder<SpamCheck> {
        private SourceType source;
        private int similarLimit;

        Builder() {}

        @Required
        public Builder source(SourceType source){
            this.source = source;
            return this;
        }

        @Required
        public Builder similarLimit(int limit) {
            this.similarLimit = limit;
            return this;
        }

        @Override
        public @NotNull SpamCheck build(){
            requireNonNull(source);
            return new SpamCheck(source, Math.max(2, similarLimit));
        }
    }
}
