package net.dreamerzero.chatregulator.modules;

import net.dreamerzero.chatregulator.utils.InfractionPlayer;

public class SpamUtils {
    public boolean messageSpamInfricted(InfractionPlayer infractionPlayer, String actualMessage){
        String prelasMessage = infractionPlayer.preLastMessage();
        String lastMessage = infractionPlayer.lastMessage();

         return prelasMessage == lastMessage && lastMessage == actualMessage;
    }

    public boolean commandSpamInfricted(InfractionPlayer infractionPlayer, String actualCommand){
        String prelastCommand = infractionPlayer.preLastCommand();
        String lastCommand = infractionPlayer.lastCommand();

         return prelastCommand == lastCommand && lastCommand == actualCommand;
    }
}
