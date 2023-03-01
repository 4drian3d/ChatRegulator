package io.github._4drian3d.chatregulator.api.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class Commands {
    /**
     * Get the first argument of a string
     * @param string the string
     * @return the first argument
     */
    public static @NotNull String getFirstArgument(final @NotNull String string) {
        final int index = Objects.requireNonNull(string).indexOf(" ");
        if (index == -1) {
            return string;
        }
        return string.substring(0, index);
    }

    /**
     * Check if a string starts with another string, checking for its arguments
     * If the second string has a "*" symbol at the end,
     * it will be checked by means of a {@link String#startsWith(String)}
     * @param string the base string
     * @param startingString the starting string
     * @return if a string starts with another string
     */
    public static boolean isStartingString(@NotNull String string, @NotNull String startingString){
        if(Objects.requireNonNull(string).length() < Objects.requireNonNull(startingString).length()){
            return false;
        }
        startingString = startingString.toLowerCase(Locale.ROOT);
        string = string.toLowerCase(Locale.ROOT);
        return string.equals(startingString) ||
                string.startsWith(
                        getLastChar(startingString) == '*'
                                ? startingString.substring(0, startingString.length()-1)
                                : startingString.concat(" ")
                );
    }

    /**
     * Get the last character of a string
     * @param string the string
     * @return the last character
     */
    public static char getLastChar(@NotNull String string){
        return string.charAt(string.length()-1);
    }

}
