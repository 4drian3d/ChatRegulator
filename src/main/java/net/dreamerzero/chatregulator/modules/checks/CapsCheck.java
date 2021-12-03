package net.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;

import net.dreamerzero.chatregulator.config.Configuration;

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
}
