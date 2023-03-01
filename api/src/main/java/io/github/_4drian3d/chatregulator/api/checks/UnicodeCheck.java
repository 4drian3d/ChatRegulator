package io.github._4drian3d.chatregulator.api.checks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.ReplaceableResult;
import io.github._4drian3d.chatregulator.api.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Check for invalid characters
 */
public final class UnicodeCheck implements ICheck {
    private final char[] chars;
    private final ControlType control;
    private final Predicate<Character> charPredicate;

    private UnicodeCheck(char[] chars, ControlType control, CharMode mode) {
        this.chars = chars;
        this.control = control;
        if (chars == null) {
            this.charPredicate = UnicodeCheck::defaultCharTest;
        } else {
            this.charPredicate = (mode == CharMode.BLACKLIST)
                    ? c -> defaultCharTest(c) || charTest(c)
                    : c -> defaultCharTest(c) && !charTest(c);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return A {@link Result} if the check was not successful or if the {@link ControlType} is {@link ControlType#BLOCK}
     * else it will return a {@link ReplaceableResult}
     */
    @Override
    public @NotNull CompletableFuture<Result> check(final @NotNull String string) {
        return CompletableFuture.supplyAsync(() -> {
            final char[] charArray = Objects.requireNonNull(string).toCharArray();
            final Set<Character> results = new HashSet<>(charArray.length);

            for (final char character : charArray) {
                if (charPredicate.test(character)) {
                    if (control == ControlType.BLOCK) {
                        return new Result(string, true);
                    }
                    results.add(character);
                }
            }

            return results.isEmpty()
                    ? new Result(string, false)
                    : new ReplaceableResult(results.toString(), true) {
                @Override
                public String replaceInfraction() {
                    String replaced = string;
                    for (final char character : results) {
                        replaced = replaced.replace(character, ' ');
                    }
                    return replaced;
                }
            };
        });
    }

    public static boolean defaultCharTest(char c) {
        if (c >= ' ' && c <= '~') {
            return false;
        }
        if (c <= '¿') {
            return false;
        }
        return !(c <= 'þ');
    }

    private boolean charTest(char c) {
        for (final char character : this.chars) {
            if (character == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }

    public static UnicodeCheck.Builder builder() {
        return new UnicodeCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<UnicodeCheck> {
        private char[] chars;
        private ControlType control = ControlType.REPLACE;
        private CharMode mode = CharMode.BLACKLIST;

        private Builder() {
        }

        /**
         * Set the blocked characters
         *
         * @param chars the characters
         * @return this
         */
        public Builder characters(char... chars) {
            this.chars = chars;
            return this;
        }

        /**
         * Set if the check can replace the infraction
         *
         * @param control the control type
         * @return this
         */
        public Builder controlType(ControlType control) {
            this.control = control;
            return this;
        }

        public Builder charMode(CharMode mode) {
            this.mode = mode;
            return this;
        }

        @Override
        public @NotNull UnicodeCheck build() {
            return new UnicodeCheck(chars, control, mode);
        }

    }

    public enum CharMode {
        WHITELIST, BLACKLIST
    }
}
