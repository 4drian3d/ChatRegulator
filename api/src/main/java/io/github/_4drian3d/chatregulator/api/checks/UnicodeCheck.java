package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Check for invalid characters
 */
public final class UnicodeCheck implements ICheck {
    private final char[] chars;
    private final ControlType control;
    private final Predicate<Character> charPredicate;

    private UnicodeCheck(char[] chars, ControlType control, DetectionMode mode) {
        this.chars = chars;
        this.control = control;
        if (chars == null) {
            this.charPredicate = UnicodeCheck::defaultCharTest;
        } else {
            this.charPredicate = (mode == DetectionMode.BLACKLIST)
                    ? c -> defaultCharTest(c) || charTest(c)
                    : c -> defaultCharTest(c) && !charTest(c);
        }
    }

    public static boolean defaultCharTest(char c) {
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
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, final @NotNull String string) {
        final char[] charArray = requireNonNull(string).toCharArray();
        final Set<Character> results = new HashSet<>(charArray.length);

        for (final char character : charArray) {
            if (charPredicate.test(character)) {
                if (control == ControlType.BLOCK) {
                    return CheckResult.denied(type());
                }
                results.add(character);
            }
        }

        if (results.isEmpty()) {
            return CheckResult.allowed();
        } else {
            String replaced = string;
            for (final char character : results) {
                replaced = replaced.replace(character, ' ');
            }
            return CheckResult.modified(replaced);
        }
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
        private DetectionMode mode = DetectionMode.BLACKLIST;

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

        public Builder detectionMode(DetectionMode mode) {
            this.mode = mode;
            return this;
        }

        @Override
        public @NotNull UnicodeCheck build() {
            requireNonNull(control);
            return new UnicodeCheck(chars, control, mode);
        }

    }
}
