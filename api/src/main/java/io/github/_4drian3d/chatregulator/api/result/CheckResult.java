package io.github._4drian3d.chatregulator.api.result;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Result of any check
 */
public sealed interface CheckResult {
    /**
     * Successful detection in a check
     *
     * @param type the InfractionType detected
     * @return the result
     */
    static @NotNull CheckResult denied(final @NotNull InfractionType type) {
        return new DeniedCheckResult(type);
    }

    /**
     * Allowed result of a check
     *
     * @return the result
     */
    static @NotNull CheckResult allowed() {
        return AllowedCheckResult.INSTANCE;
    }

    /**
     * Successful detection in a check
     * <p>Contrary to a denied result, it must be modified as configured in its creation</p>
     *
     * @param modifier the modified result
     * @return the result
     */
    static @NotNull CheckResult modified( final @NotNull InfractionType infractionType, final @NotNull String modifier) {
        return new ReplaceCheckResult(requireNonNull(infractionType), requireNonNull(modifier));
    }

    /**
     * Check if a check has been unsuccessful and that the detection chain can be followed
     *
     * @return true if the check was not successful
     */
    boolean isAllowed();

    boolean isDenied();

    boolean shouldModify();

    final class AllowedCheckResult implements CheckResult {
        private static final AllowedCheckResult INSTANCE = new AllowedCheckResult();

        private AllowedCheckResult() {}

        @Override
        public boolean isAllowed() {
            return true;
        }

        @Override
        public boolean isDenied() {
            return false;
        }

        @Override
        public boolean shouldModify() {
            return false;
        }
    }

    record DeniedCheckResult(InfractionType infractionType) implements CheckResult, DetectedResult {
        @Override
        public boolean isAllowed() {
            return false;
        }

        @Override
        public boolean isDenied() {
            return true;
        }

        @Override
        public boolean shouldModify() {
            return false;
        }
    }

    record ReplaceCheckResult(InfractionType infractionType, String modified) implements CheckResult, DetectedResult {
        @Override
        public boolean isAllowed() {
            return false;
        }

        @Override
        public boolean isDenied() {
            return false;
        }

        @Override
        public boolean shouldModify() {
            return true;
        }

        public String replaced() {
            return modified;
        }
    }

    sealed interface DetectedResult {
        InfractionType infractionType();
    }
}
