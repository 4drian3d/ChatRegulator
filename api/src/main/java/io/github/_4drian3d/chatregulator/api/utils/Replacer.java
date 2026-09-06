package io.github._4drian3d.chatregulator.api.utils;

import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

/**
 * String Replacer Utils
 */
public final class Replacer {
    public static final Pattern NORMALIZATION_PATTERN = Pattern.compile("\\p{M}");

    private Replacer() {
    }
    /**
     * Converts a string with the first character converted to uppercase
     *
     * @param string the string
     * @return a string with the first character converted to uppercase
     */
    public static @NotNull String firstLetterUppercase(@NotNull final String string) {
        if (Objects.requireNonNull(string).isEmpty()) {
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
        if (string.length() <= 1 || string.length() >= 256 || string.charAt(string.length() - 1) == '.') {
            return string;
        }

        return string + ".";
    }

    /**
     * Normalizes a Unicode string to the specified form, truncating to 256 characters if necessary.
     * <br>
     * This method applies Unicode normalization (NFC, NFKC, etc.) to handle variant representations
     * of the same character. The result is truncated to 256 characters to prevent overflow.
     *
     * @param string the input string to normalize
     * @param form the Unicode normalization form to apply
     * @return the normalized string, truncated to at most 256 characters
     */
    public static @NotNull String unicodeNormalize(@NotNull final String string, @NotNull final Normalizer.Form form) {
        Objects.requireNonNull(string);
        final String normalized = NORMALIZATION_PATTERN.matcher(Normalizer.normalize(string, form)).replaceAll("");
        return normalized.length() > 256 ? normalized.substring(0, 256) : normalized;
    }

    /**
     * Applies a trailing dot and a leading capital letter to the specified string
     *
     * @param string the string
     * @return the string converted
     */
    public static @NotNull String applyFormat(final @NotNull String string) {
        return firstLetterUppercase(addFinalDot(unicodeNormalize(string, Normalizer.Form.NFC)));
    }
}
