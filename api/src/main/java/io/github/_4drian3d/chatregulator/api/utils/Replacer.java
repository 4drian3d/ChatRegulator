package io.github._4drian3d.chatregulator.api.utils;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * String Replacer Utils
 */
public final class Replacer {
    private Replacer() {
    }
    /**
     * Converts a string with the first character converted to uppercase
     *
     * @param string the string
     * @return a string with the first character converted to uppercase
     */
    public static @NotNull String firstLetterUppercase(@NotNull final String string) {
        if (Objects.requireNonNull(string).length() < 1) {
            return string;
        }

        final char firstCharacter = string.charAt(0);
        if (Character.isUpperCase(firstCharacter)) return string;

        return Character.toUpperCase(firstCharacter) +
                string.substring(1);
    }

    /**
     * Add a dot at the end of a string
     * <p>
     * If the string already has an endpoint, it will return the same string
     *
     * @param string the string
     * @return the string converted
     */
    public static @NotNull String addFinalDot(@NotNull final String string) {
        Objects.requireNonNull(string);
        if (string.length() <= 1 || string.charAt(string.length() - 1) == '.') {
            return string;
        }

        return string + ".";
    }

    /**
     * Applies a trailing dot and a leading capital letter to the specified string
     *
     * @param string the string
     * @return the string converted
     */
    public static @NotNull String applyFormat(final @NotNull String string) {
        return firstLetterUppercase(addFinalDot(string));
    }
}
