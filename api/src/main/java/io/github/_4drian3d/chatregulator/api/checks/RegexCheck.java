package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.*;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Check for detecting and handling restricted words using regex patterns.
 * <br>
 * This check matches provided regex patterns against the input string. If matches
 * are found, it can either deny the message ({@link ControlType#BLOCK}) or replace the matched
 * parts with asterisks ({@link ControlType#REPLACE}).
 *
 * @see ControlType
 * @see Pattern
 */
public final class RegexCheck implements Check {
    private final Pattern[] blockedWords;
    private final ControlType controlType;

    private RegexCheck(ControlType controlType, Pattern... blockedWords) {
        this.blockedWords = blockedWords;
        this.controlType = controlType;
    }

    @Override
    public @NotNull CheckResult check(final @NotNull InfractionPlayer player, final @NotNull String string) {
        final List<Pattern> patterns = new ArrayList<>();
        for (final Pattern pattern : blockedWords) {
            final Matcher match = pattern.matcher(string);
            if (match.find()) {
                if (controlType == ControlType.BLOCK) {
                    return CheckResult.denied(type());
                }
                patterns.add(pattern);
            }
        }

        if (!patterns.isEmpty()) {
            String replaced = string;
            for (final Pattern pattern : patterns) {
                replaced = pattern.matcher(replaced).replaceAll(RegexCheck::generateReplacement);
            }
            return CheckResult.modified(type(), replaced);
        } else {
            return CheckResult.allowed();
        }
    }

    /**
     * Generates a replacement string for a matched regex pattern.
     * <br>
     * The replacement consists of asterisks, with the count being half the length
     * of the matched text. This provides a visual replacement that obscures the
     * matched word while maintaining some indication of its original length.
     *
     * @param result the match result from the regex matching
     * @return a string of asterisks representing the replacement
     */
    public static String generateReplacement(final MatchResult result) {
        final int size = result.group().length() / 2;
        return "*".repeat(size);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGEX;
    }

    /**
     * Creates a new Builder
     *
     * @return a new RegexCheck Builder
     */
    public static @NotNull Builder builder() {
        return new RegexCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<RegexCheck> {
        private Collection<Pattern> blockedWords;
        private ControlType controlType;

        private Builder() {
        }

        /**
         * Add regex patterns to the list of blocked patterns.
         * <br>
         * Can be called multiple times to accumulate patterns.
         *
         * @param patterns the regex patterns to block
         * @return this builder for chaining
         * @throws NullPointerException if patterns or any pattern is null
         */
        public Builder blockedPatterns(final @NotNull Collection<@NotNull Pattern> patterns) {
            requireNonNull(patterns);
            if (this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(patterns);
            } else {
                this.blockedWords.addAll(patterns);
            }
            return this;
        }

        /**
         * Add regex patterns to the list of blocked patterns.
         * <br>
         * Can be called multiple times to accumulate patterns.
         *
         * @param patterns the regex patterns to block
         * @return this builder for chaining
         * @throws NullPointerException if any pattern is null
         */
        public Builder blockedPatterns(final @NotNull Pattern @NotNull ... patterns) {
            if (this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(List.of(patterns));
            } else {
                Collections.addAll(this.blockedWords, patterns);
            }
            return this;
        }

        /**
         * Set the control type for this check (BLOCK or REPLACE).
         * <ul>
         *  <li>{@link ControlType#BLOCK}: Deny the message if any pattern matches</li>
         *  <li>{@link ControlType#REPLACE}: Replace matches with asterisks</li>
         * </ul>
         *
         * @param controlType the control type to apply
         * @return this builder for chaining
         * @throws NullPointerException if controlType is null
         */
        @Required
        public Builder controlType(final @NotNull ControlType controlType) {
            this.controlType = requireNonNull(controlType);
            return this;
        }

        @Override
        public @NotNull RegexCheck build() {
            if (this.blockedWords == null) {
                this.blockedWords = Collections.emptySet();
            }
            requireNonNull(controlType);
            return new RegexCheck(controlType, blockedWords.toArray(new Pattern[0]));
        }
    }
}
