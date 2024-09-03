package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

/**
 * Check for invalid characters
 */
public final class UnicodeCheck implements Check {
    private final @NotNull Set<Integer> chars;
    private final ControlType charControl;
    private final Predicate<Integer> charPredicate;

    private final @NotNull Set<Character.UnicodeBlock> blocks;
    private final ControlType blockControl;
    private final Predicate<Integer> blockPredicate;

    private final @NotNull Set<Character.UnicodeScript> scripts;
    private final ControlType scriptControl;
    private final Predicate<Integer> scriptPredicate;

    private UnicodeCheck(Integer @NotNull [] chars, ControlType charControl, DetectionMode charMode,
                         Character.UnicodeBlock @NotNull [] blocks, ControlType blockControl, DetectionMode blockMode,
                         Character.UnicodeScript @NotNull [] scripts, ControlType scriptControl, DetectionMode scriptMode) {
        this.chars = Set.of(chars);
        this.charControl = charControl;
        this.charPredicate = (charMode == DetectionMode.BLACKLIST) ? this.chars::contains : Predicate.not(this.chars::contains);

        this.blocks = Set.of(blocks);
        this.blockControl = blockControl;
        final Predicate<Integer> blockPredicate = codePoint -> this.blocks.contains(Character.UnicodeBlock.of(codePoint));
        this.blockPredicate = (blockMode == DetectionMode.BLACKLIST) ? blockPredicate : blockPredicate.negate();

        this.scripts = Set.of(scripts);
        this.scriptControl = scriptControl;
        final Predicate<Integer> scriptPredicate = codePoint -> this.scripts.contains(Character.UnicodeScript.of(codePoint));
        this.scriptPredicate = (scriptMode == DetectionMode.BLACKLIST) ? scriptPredicate : scriptPredicate.negate();
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, final @NotNull String string) {
        final List<Integer> codePointList = requireNonNull(string).codePoints().boxed().collect(Collectors.toList());
        boolean replaced = false;

        if (this.charControl == ControlType.BLOCK) {
            if (codePointList.stream().anyMatch(this.charPredicate)) {
                return CheckResult.denied(type());
            }
        } else {
            replaced |= codePointList.removeIf(this.charPredicate);
        }

        if (this.blockControl == ControlType.BLOCK) {
            if (codePointList.stream().anyMatch(this.blockPredicate)) {
                return CheckResult.denied(type());
            }
        } else {
            replaced |= codePointList.removeIf(this.blockPredicate);
        }

        if (this.scriptControl == ControlType.BLOCK) {
            if (codePointList.stream().anyMatch(this.scriptPredicate)) {
                return CheckResult.denied(type());
            }
        } else {
            replaced |= codePointList.removeIf(this.scriptPredicate);
        }

        if (replaced) {
            return CheckResult.modified(type(), codePointList.stream()
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString());
        } else {
            return CheckResult.allowed();
        }
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }

    /**
     * Creates a new Builder
     *
     * @return a new UnicodeCheck Builder
     */
    public static UnicodeCheck.Builder builder() {
        return new UnicodeCheck.Builder();
    }

    /**
     * Unicode Check Builder
     */
    public static class Builder implements AbstractBuilder<UnicodeCheck> {
        private Integer @Nullable [] chars;
        private ControlType charControl = ControlType.REPLACE;
        private DetectionMode charMode = DetectionMode.BLACKLIST;

        private Character.UnicodeBlock @Nullable [] blocks;
        private ControlType blockControl = ControlType.REPLACE;
        private DetectionMode blockMode = DetectionMode.BLACKLIST;

        private Character.UnicodeScript @Nullable [] scripts;
        private ControlType scriptControl = ControlType.REPLACE;
        private DetectionMode scriptMode = DetectionMode.BLACKLIST;

        private Builder() {
        }

        /**
         * Set the characters to check
         *
         * @param chars the characters
         * @return this
         */
        public Builder characters(final @NotNull Integer @NotNull ... chars) {
            this.chars = chars;
            return this;
        }

        /**
         * Set if the character check can replace the infraction
         *
         * @param control the control type
         * @return this
         */
        public Builder charControlType(final @NotNull ControlType control) {
            this.charControl = control;
            return this;
        }

        /**
         * Set only allowing or denying the characters
         *
         * @param mode the detection mode
         * @return this
         */
        public Builder charDetectionMode(final @NotNull DetectionMode mode) {
            this.charMode = mode;
            return this;
        }

        /**
         * Set the unicode blocks to check
         *
         * @param blocks the unicode blocks
         * @return this
         */
        public Builder blocks(final @NotNull Character.UnicodeBlock @NotNull ... blocks) {
            this.blocks = blocks;
            return this;
        }

        /**
         * Set if the unicode block check can replace the infraction
         *
         * @param control the control type
         * @return this
         */
        public Builder blockControlType(final @NotNull ControlType control) {
            this.blockControl = control;
            return this;
        }

        /**
         * Set only allowing or denying the unicode blocks
         *
         * @param mode the detection mode
         * @return this
         */
        public Builder blockDetectionMode(final @NotNull DetectionMode mode) {
            this.blockMode = mode;
            return this;
        }

        /**
         * Set the unicode scripts to check
         *
         * @param scripts the unicode scripts
         * @return this
         */
        public Builder scripts(final @NotNull Character.UnicodeScript @NotNull ... scripts) {
            this.scripts = scripts;
            return this;
        }

        /**
         * Set if the unicode script check can replace the infraction
         *
         * @param control the control type
         * @return this
         */
        public Builder scriptControlType(final @NotNull ControlType control) {
            this.scriptControl = control;
            return this;
        }

        /**
         * Set only allowing or denying the unicode blocks
         *
         * @param mode the detection mode
         * @return this
         */
        public Builder scriptDetectionMode(final @NotNull DetectionMode mode) {
            this.scriptMode = mode;
            return this;
        }

        @Override
        public @NotNull UnicodeCheck build() {
            return new UnicodeCheck(requireNonNullElse(chars, new Integer[]{}), charControl, charMode,
                    requireNonNullElse(blocks, new Character.UnicodeBlock[]{}), blockControl, blockMode,
                    requireNonNullElse(scripts, new Character.UnicodeScript[]{}), scriptControl, scriptMode);
        }

    }
}
