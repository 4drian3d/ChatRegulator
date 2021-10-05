package net.dreamerzero.chatregulator.modules;

import net.dreamerzero.chatregulator.InfractionPlayer;

/**
 * Detection of command/message spamming
 */
public class SpamCheck {
    private InfractionPlayer infractionPlayer;
    /**
     * Create a new spam test
     * @param infractionPlayer the infractionPlayer involucred
     */
    public SpamCheck(InfractionPlayer infractionPlayer){
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
