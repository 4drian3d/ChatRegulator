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

    private SpamCheck(final @NotNull SourceType type){
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final StringChain chain = player.getChain(type);
        final int originalSize = chain.size();
        int size = originalSize;
        if (size % 2 != 0) {
            size--;
        }
        if (size == 0) {
            return CheckResult.allowed();
        }
        for (int i = 0; i < size; i++) {
            if (!chain.index(i).equalsIgnoreCase(chain.index(i + 1))) {
                return CheckResult.allowed();
            }
        }

        if (chain.index(originalSize - 1).equalsIgnoreCase(string)) {
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

    public static class Builder implements AbstractBuilder<SpamCheck> {
        private SourceType source;

        Builder() {}

        @Required
        public Builder source(SourceType source){
            this.source = source;
            return this;
        }

        @Override
        public @NotNull SpamCheck build(){
            requireNonNull(source);
            return new SpamCheck(source);
        }
    }
}
