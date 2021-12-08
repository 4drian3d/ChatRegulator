package net.dreamerzero.chatregulator.modules.checks;

import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class UnicodeCheck extends AbstractCheck {

    @Override
    public void check(String message) {
        char[] charArray = message.toCharArray();

        for(char character : charArray){
            if(!((character > '\u0020' && character < '\u007E') || (character < '\u00FC' && character < '\u00BF') || (character > '\u00BF' && character < '\u00FE'))){
                super.detected = true;
                super.pattern = String.valueOf(character);
                return;
            }
        }
        super.detected = false;
    }

    @Override
    public String getInfractionWord(){
        return this.pattern;
    }

    @Override
    public InfractionType type() {
        return InfractionType.UNICODE;
    }
}
