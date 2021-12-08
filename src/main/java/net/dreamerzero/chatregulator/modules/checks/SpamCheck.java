package net.dreamerzero.chatregulator.modules.checks;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;

/**
 * Detection of command/message spamming
 */
public class SpamCheck extends AbstractCheck {
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
        super.string = message;
        this.pattern = message;
        this.spamInfricted(message, type);
    }

    @Override
    public String getInfractionWord(){
        return super.string;
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detectec as true
     * @param string the ultimate command/message
     * @param type the source type
     */
    private void spamInfricted(String string, SourceType type){
        if(type == SourceType.CHAT){
            String prelastMessage = infractionPlayer.preLastMessage();
            String lastMessage = infractionPlayer.lastMessage();

            super.detected = prelastMessage.equalsIgnoreCase(lastMessage) && lastMessage.contains(string);
        } else {
            String prelastCommand = infractionPlayer.preLastCommand();
            String lastCommand = infractionPlayer.lastCommand();

            super.detected = prelastCommand.equalsIgnoreCase(lastCommand) && lastCommand.contains(string);
        }
    }

    @Override
    public InfractionType type() {
        return InfractionType.SPAM;
    }
}
