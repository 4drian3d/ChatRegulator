package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.annotations.Required;
import io.github._4drian3d.chatregulator.api.enums.CapsAlgorithm;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static java.util.Objects.requireNonNull;

/**
 * Check for compliance with uppercase character limit in a string
 */
public final class CapsCheck implements Check {
    private final int limit;
    private final ControlType controlType;
    private final CapsAlgorithm algorithm;

    private CapsCheck(int limit, ControlType controlType, CapsAlgorithm algorithm) {
        this.limit = limit;
        this.controlType = controlType;
        this.algorithm = algorithm;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final long caps = requireNonNull(string)
                .chars()
                .filter(Character::isUpperCase)
                .count();
        final boolean surpassedLimit = switch (algorithm) {
            case AMOUNT -> caps >= this.limit;
            case PERCENTAGE -> {
                final double length = string.length();
                final double percentageLimit = (length / 100) * this.limit;
                yield caps >= percentageLimit;
            }
        };

        if (surpassedLimit) {
            if (controlType == ControlType.REPLACE) {
                return CheckResult.modified(string.toLowerCase(Locale.ROOT));
            } else {
                return CheckResult.denied(type());
            }
        } else {
            return CheckResult.allowed();
        }
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.CAPS;
    }

    /**
     * Create a builder
     * @return a new CapsCheck.Builder
     */
    public static CapsCheck.Builder builder(){
        return new CapsCheck.Builder();
    }

    /**Caps Check Builder */
    public static class Builder implements AbstractBuilder<CapsCheck> {
        private int limit;
        private ControlType controlType = ControlType.BLOCK;
        private CapsAlgorithm algorithm = CapsAlgorithm.AMOUNT;
        Builder() {}

        /**
         * Set the new caps limit
         * @param limit the new limit
         * @return this
         */
        public Builder limit(@NonNegative int limit) {
            this.limit = limit;
            return this;
        }

        @Required
        public Builder controlType(ControlType controlType) {
            this.controlType = controlType;
            return this;
        }

        public Builder algorithm(CapsAlgorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        @Override
        public @NotNull CapsCheck build(){
            return new CapsCheck(limit, controlType, algorithm);
        }
    }
}
