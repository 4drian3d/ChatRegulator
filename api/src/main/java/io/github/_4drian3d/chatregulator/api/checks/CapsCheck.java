package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/**
 * Check for compliance with uppercase character limit in a string
 */
public final class CapsCheck implements ICheck {
    private final int limit;
    private final ControlType controlType;

    private CapsCheck(int limit, ControlType controlType){
        this.limit = limit;
        this.controlType = controlType;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        boolean aboveLimit = Objects.requireNonNull(string)
                .chars()
                .filter(Character::isUpperCase)
                .count() >= this.limit;
        if (aboveLimit) {
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
        private ControlType controlType;
        Builder(){}

        /**
         * Set the new caps limit
         * @param limit the new limit
         * @return this
         */
        public Builder limit(int limit){
            this.limit = limit;
            return this;
        }

        public Builder controlType(ControlType controlType) {
            this.controlType = controlType;
            return this;
        }

        @Override
        public @NotNull CapsCheck build(){
            return new CapsCheck(limit, controlType);
        }
    }
}
