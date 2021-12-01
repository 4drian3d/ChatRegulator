package net.dreamerzero.chatregulator.modules;

import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;

public class Replacer {
    private MainConfig.Config config;
    public Replacer(){
        this.config = Configuration.getConfig();
    }
    public String firstLetterUpercase(String string){
        if(!config.getFormatConfig().setFirstLetterUppercase() || string.length() < 1)
            return string;

        char firstCharacter = string.charAt(0);
        if(Character.isUpperCase(firstCharacter)) return string;

        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(firstCharacter)).append(string.substring(1));
        return builder.toString();
    }

    public String addFinalDot(String string){
        if(!config.getFormatConfig().setFinalDot() || string.charAt(string.length()-1)=='.' || string.length() <= 1)
            return string;

        return string.concat(".");
    }

    public String applyFormat(String string){
        return firstLetterUpercase(addFinalDot(string));
    }
}
