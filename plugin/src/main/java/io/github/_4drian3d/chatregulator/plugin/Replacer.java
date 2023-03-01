package io.github._4drian3d.chatregulator.plugin;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.plugin.config.Configuration;

/**
 * Replacer class
 */
public final class Replacer {
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

        return new StringBuilder(string.length())
                .append(Character.toUpperCase(firstCharacter))
                .append(string.substring(1))
                .toString();
    }

    public static @NotNull String firstLetterUppercase(@NotNull final String string, Configuration config) {
        if (!config.getFormatConfig().setFirstLetterUppercase()) return string;
        return firstLetterUppercase(string);
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

    public static String addFinalDot(final String string, Configuration config) {
        return config.getFormatConfig().setFinalDot()
                ? addFinalDot(string)
                : string;
    }

    private Replacer() {
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

    public static @NotNull String applyFormat(final @NotNull String string, Configuration config) {
        return firstLetterUppercase(addFinalDot(string, config), config);
    }
}
