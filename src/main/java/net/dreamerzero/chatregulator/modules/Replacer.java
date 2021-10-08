package net.dreamerzero.chatregulator.modules;

import de.leonhard.storage.Yaml;

public class Replacer {
    private Yaml config;
    public Replacer(Yaml config){
        this.config = config;
    }
    public String firstLetterUpercase(String string){
        char firstCharacter = string.charAt(0);
        if(Character.isUpperCase(firstCharacter) ||
            !config.getBoolean("format.set-first-letter-uppercase")) {
                return string;
            }

        StringBuilder builder = new StringBuilder();
        builder.append(firstCharacter).append(string.substring(1));
        return builder.toString();
    }

    public String addFinalDot(String string){
        if(string.charAt(string.length()-1)=='.' ||
            !config.getBoolean("format.set-final-dot")) return string;

        return string.concat(".");
    }

    public String applyFormat(String string){
        return firstLetterUpercase(addFinalDot(string));
    }
}
