package me.dreamerzero.chatregulator.modules;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;

/**
 * Replacer class
 */
public final class Replacer {
    private final MainConfig.Format config;
    public Replacer(){
        this.config = Configuration.getConfig().getFormatConfig();
    }

    /**
     * Converts a string with the first character converted to uppercase
     * @param string the string
     * @return a string with the first character converted to uppercase
     */
    public @NotNull String firstLetterUpercase(@NotNull final String string){
        Objects.requireNonNull(string);
        if(!config.setFirstLetterUppercase() || string.length() < 1)
            return string;

        char firstCharacter = string.charAt(0);
        if(Character.isUpperCase(firstCharacter)) return string;

        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(firstCharacter)).append(string.substring(1));
        return builder.toString();
    }

    /**
     * Add a dot at the end of a string
     *
     * If the string already has an endpoint, it will return the same string
     * @param string the string
     * @return the string converted
     */
    public @NotNull String addFinalDot(@NotNull final String string){
        Objects.requireNonNull(string);
        if(!config.setFinalDot() || string.charAt(string.length()-1)=='.' || string.length() <= 1)
            return string;

        return string.concat(".");
    }

    /**
     * Applies a trailing dot and a leading capital letter to the specified string
     * @param string the string
     * @return the string converted
     */
    public @NotNull String applyFormat(@NotNull final String string){
        return this.firstLetterUpercase(this.addFinalDot(Objects.requireNonNull(string)));
    }
}
