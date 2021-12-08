package me.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;

public class CapsCheck extends AbstractCheck{

    @Override
    public void check(String message) {
        super.string = message;
        char[] chararray = message.toCharArray();
        int capcount = 0;
        for(char c : chararray){
            if(Character.isUpperCase(c)) capcount++;
        }

        if(capcount >= Configuration.getConfig().getCapsConfig().limit()){
            super.detected = true;
            super.pattern = message;
        }
    }

    public String replaceInfraction(){
        return super.string.toLowerCase(Locale.ROOT);
    }

    @Override
    public String getInfractionWord(){
        return this.string;
    }

    @Override
    public InfractionType type() {
        return InfractionType.CAPS;
    }
}
