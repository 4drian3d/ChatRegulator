package me.dreamerzero.chatregulator.modules.checks;

import org.jetbrains.annotations.Nullable;

public interface ReplaceableCheck {
    /**
     * Replaces the violations of a string
     * according to the corresponding check
     *
     * May return null if set to not replace
     * the violation or if the check is not positive.
     * @return the string replaced
     */
    public @Nullable String replaceInfraction();
}
