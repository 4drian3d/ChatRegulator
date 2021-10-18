package net.dreamerzero.chatregulator.modules.checks;

public class UnicodeCheck extends Check {

    @Override
    public void check(String message) {
        char charArray[] = message.toCharArray();

        for(char character : charArray){
            if((character > '\u0020' && character < '\u007E') || (character < '\u00FC' && character < '\u00BF')){
                continue;
            } else {
                super.detected = true;
                super.pattern = String.valueOf(character);
                break;
            }
        }
        super.detected = false;
    }

    @Override
    public String getInfractionWord(){
        return this.pattern;
    }
}
