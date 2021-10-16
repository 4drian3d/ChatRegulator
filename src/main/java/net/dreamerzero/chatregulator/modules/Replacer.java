package net.dreamerzero.chatregulator.modules;

import de.leonhard.storage.Yaml;

public class Replacer {
    private Yaml config;
    public Replacer(Yaml config){
        this.config = config;
    }
    public String firstLetterUpercase(String string){
        if(string.length() < 1 ||
        !config.getBoolean("format.set-first-letter-uppercase")) return string;

        char firstCharacter = string.charAt(0);
        if(Character.isUpperCase(firstCharacter)) return string;

        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(firstCharacter)).append(string.substring(1));
        return builder.toString();
    }

    public String addFinalDot(String string){
        if(string.length() <= 1 || string.charAt(string.length()-1)=='.'
        || !config.getBoolean("format.set-final-dot")) return string;

        return string.concat(".");
    }

    public String applyFormat(String string){
        return firstLetterUpercase(addFinalDot(string));
    }
}
