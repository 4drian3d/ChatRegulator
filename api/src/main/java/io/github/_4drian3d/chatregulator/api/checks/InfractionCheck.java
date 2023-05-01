package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.*;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Utilities for the detection of restricted words
 */
public final class InfractionCheck implements ICheck {
    private final Pattern[] blockedWords;
    private final ControlType controlType;

    private InfractionCheck(ControlType controlType, Pattern... blockedWords) {
        this.blockedWords = blockedWords;
        this.controlType = controlType;
    }

    @Override
    public @NotNull CheckResult check(final @NotNull InfractionPlayer player, final @NotNull String string) {
        final List<Matcher> matchers = new ArrayList<>();
        boolean detected = false;
        for (final Pattern pattern : blockedWords) {
            final Matcher match = pattern.matcher(string);
            if (match.find()) {
                detected = true;
                if (controlType == ControlType.BLOCK) {
                    return CheckResult.denied();
                }
                matchers.add(match);
            }
        }

        if (detected) {
            String replaced = string;
            for (final Matcher matcher : matchers) {
                replaced = matcher.replaceAll(InfractionCheck::generateReplacement);
            }
            return CheckResult.modified(replaced);
        } else {
            return CheckResult.allowed();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param string the string
     * @return May return a  if the result was successful
     * and the check was set to only block the message.
     * Or a CheckResult if the check was successful
     * and is configured to replace multiple violations.
     * Or a {@link Result} if the check was not successful
     * @see ICheck
     */

    public static String generateReplacement(final MatchResult result) {
        final int size = result.group().length() / 2;
        return "*".repeat(size);
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.REGULAR;
    }

    public static @NotNull InfractionCheck.Builder builder() {
        return new InfractionCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<InfractionCheck> {
        private Set<Pattern> blockedWords;
        private ControlType controlType;

        private Builder() {
        }

        public Builder blockedPattern(Collection<Pattern> patterns) {
            requireNonNull(patterns);
            if (this.blockedWords == null) {
                this.blockedWords = new LinkedHashSet<>(patterns);
            } else {
                this.blockedWords.addAll(patterns);
            }
            return this;
        }

        public Builder blockedPatterns(Pattern... patterns) {
            if (this.blockedWords == null) {
                this.blockedWords = new LinkedHashSet<>(List.of(patterns));
            } else {
                Collections.addAll(this.blockedWords, patterns);
            }
            return this;
        }

        public Builder controlType(ControlType controlType) {
            this.controlType =  requireNonNull(controlType);
            return this;
        }

        @Override
        public @NotNull InfractionCheck build() {
            if (this.blockedWords == null) {
                this.blockedWords = Collections.emptySet();
            }
            requireNonNull(controlType);
            return new InfractionCheck(controlType, blockedWords.toArray(new Pattern[0]));
        }
    }
}
