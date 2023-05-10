package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
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
 * Utilities for the detection of restricted words
 */
public final class RegexCheck implements ICheck {
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

        if (patterns.size() != 0) {
            String replaced = string;
            for (final Pattern pattern : patterns) {
                replaced = pattern.matcher(replaced).replaceAll(RegexCheck::generateReplacement);
            }
            return CheckResult.modified(replaced);
        } else {
            return CheckResult.allowed();
        }
    }

    public static String generateReplacement(final MatchResult result) {
        final int size = result.group().length() / 2;
        return "*".repeat(size);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGEX;
    }

    public static @NotNull RegexCheck.Builder builder() {
        return new RegexCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<RegexCheck> {
        private Collection<Pattern> blockedWords;
        private ControlType controlType;

        private Builder() {
        }

        public Builder blockedPatterns(Collection<Pattern> patterns) {
            requireNonNull(patterns);
            if (this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(patterns);
            } else {
                this.blockedWords.addAll(patterns);
            }
            return this;
        }

        public Builder blockedPatterns(Pattern... patterns) {
            if (this.blockedWords == null) {
                this.blockedWords = new ArrayList<>(List.of(patterns));
            } else {
                Collections.addAll(this.blockedWords, patterns);
            }
            return this;
        }

        public Builder controlType(ControlType controlType) {
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
