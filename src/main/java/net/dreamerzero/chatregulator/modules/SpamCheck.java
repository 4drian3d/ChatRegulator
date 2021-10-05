package net.dreamerzero.chatregulator.modules;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.InfractionPlayer;

public class SpamCheck {
    //For debug
    @SuppressWarnings("unused")
    private Logger logger;
    private InfractionPlayer infractionPlayer;
    public SpamCheck(Logger logger,InfractionPlayer infractionPlayer){
        this.logger = logger;
        this.infractionPlayer = infractionPlayer;
    }

    public boolean messageSpamInfricted(String actualMessage){
        String prelastMessage = infractionPlayer.preLastMessage();
        String lastMessage = infractionPlayer.lastMessage();

        return prelastMessage.equalsIgnoreCase(lastMessage) && lastMessage.equalsIgnoreCase(actualMessage);
    }

    public boolean commandSpamInfricted(String actualCommand){
        String prelastCommand = infractionPlayer.preLastCommand();
        String lastCommand = infractionPlayer.lastCommand();

         return prelastCommand == lastCommand && lastCommand == actualCommand;
    }
}
