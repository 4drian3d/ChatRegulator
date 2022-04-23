package me.dreamerzero.chatregulator.result;

import org.jetbrains.annotations.Nullable;

/**Results that may be replaceables */
public interface IReplaceable {
    /**
     * Replaces the violations of a string
     * according to the corresponding check
     *
     * May return null if set to not replace
     * the violation or if the check is not positive
     * @return the string replaced
     */
    public @Nullable String replaceInfraction();
}
