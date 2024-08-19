package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.StringChain;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.Locale;

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

        final Iterator<String> it = chain.iterator();
        String actual;
        String previous = null;
        while(it.hasNext()) {
            actual = it.next();
            if (previous != null && !actual.equals(previous)) {
                return CheckResult.allowed();
            }
            previous = actual;
        }

        if (chain.last().equals(Normalizer.normalize(string.toLowerCase(Locale.ROOT), Normalizer.Form.NFKD))) {
            return CheckResult.denied(type());
        } else {
            return CheckResult.allowed();
        }
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SPAM;
    }

    /**
     * Creates a new Builder
     *
     * @return a new SpamCheck Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Spam Check builder
     */
    public static final class Builder implements AbstractBuilder<SpamCheck> {
        private SourceType source;
        private int similarLimit;

        private Builder() {}

        @Required
        public Builder source(final @NotNull SourceType source){
            this.source = source;
            return this;
        }

        @Required
        public Builder similarLimit(final @Range(from = 2, to = 255) int limit) {
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
