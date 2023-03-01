package io.github._4drian3d.chatregulator.api.result;

import org.jetbrains.annotations.Nullable;

/**Results that may be replaceable */
public interface IReplaceable {
    /**
     * Replaces the violations of a string
     * according to the corresponding check
     *
     * May return null if set to not replace
     * the violation or if the check is not positive
     * @return the string replaced
     */
    @Nullable String replaceInfraction();
}
