package net.dreamerzero.chatregulator.modules;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.InfractionPlayer;

public class SpamCheck {
    //For debug
    @SuppressWarnings("unused")
    private Logger logger;
    private InfractionPlayer infractionPlayer;
    /**
     * Create a new spam test
     * @param logger the logger
     * @param infractionPlayer the infractionPlayer involucred
     */
    public SpamCheck(Logger logger,InfractionPlayer infractionPlayer){
        this.logger = logger;
        this.infractionPlayer = infractionPlayer;
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages.
     * @param actualMessage the ultimate message
     * @return if the {@link InfractionPlayer} is spamming
     */
    public boolean messageSpamInfricted(String actualMessage){
        String prelastMessage = infractionPlayer.preLastMessage();
        String lastMessage = infractionPlayer.lastMessage();

        return prelastMessage.equalsIgnoreCase(lastMessage) && lastMessage.equalsIgnoreCase(actualMessage);
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous commands.
     * @param actualCommand the ultimate command
     * @return if the {@link InfractionPlayer} is spamming
     */
    public boolean commandSpamInfricted(String actualCommand){
        String prelastCommand = infractionPlayer.preLastCommand();
        String lastCommand = infractionPlayer.lastCommand();

         return prelastCommand == lastCommand && lastCommand == actualCommand;
    }
}
