package net.dreamerzero.chatregulator.modules;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;

/**
 * Detection of command/message spamming
 */
public class SpamCheck extends Check {
    private final InfractionPlayer infractionPlayer;
    private final SourceType type;
    /**
     * Create a new spam test
     * @param infractionPlayer the infractionPlayer involucred
     */
    public SpamCheck(InfractionPlayer infractionPlayer, SourceType type){
        this.infractionPlayer = infractionPlayer;
        this.type = type;
    }

    @Override
    public void check(String message){
        spamInfricted(message, type);
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detectec as true
     * @param string the ultimate command/message
     * @param type the source type
     */
    void spamInfricted(String string, SourceType type){
        switch(type){
            case CHAT:
                String prelastMessage = infractionPlayer.preLastMessage();
                String lastMessage = infractionPlayer.lastMessage();

                super.detected = prelastMessage.equalsIgnoreCase(lastMessage) && lastMessage.equalsIgnoreCase(string);
                break;
            case COMMAND:
                String prelastCommand = infractionPlayer.preLastCommand();
                String lastCommand = infractionPlayer.lastCommand();

                super.detected = prelastCommand == lastCommand && lastCommand == string;
                break;
        }
    }
}
