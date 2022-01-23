package me.dreamerzero.chatregulator.modules;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;

public class Replacer {
    private final MainConfig.Format config;
    public Replacer(){
        this.config = Configuration.getConfig().getFormatConfig();
    }
    public String firstLetterUpercase(final String string){
        if(!config.setFirstLetterUppercase() || string.length() < 1)
            return string;

        char firstCharacter = string.charAt(0);
        if(Character.isUpperCase(firstCharacter)) return string;

        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(firstCharacter)).append(string.substring(1));
        return builder.toString();
    }

    public String addFinalDot(final String string){
        if(!config.setFinalDot() || string.charAt(string.length()-1)=='.' || string.length() <= 1)
            return string;

        return string.concat(".");
    }

    public String applyFormat(final String string){
        return this.firstLetterUpercase(this.addFinalDot(string));
    }
}
