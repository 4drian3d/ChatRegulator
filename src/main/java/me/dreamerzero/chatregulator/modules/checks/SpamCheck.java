package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.result.Result;

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
        this.infractionPlayer = Objects.requireNonNull(infractionPlayer);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public CompletableFuture<Result> check(@NotNull String message){
        super.string = Objects.requireNonNull(message);
        boolean infricted = this.spamInfricted();
        return CompletableFuture.completedFuture(new Result(message, infricted));
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detectec as true
     */
    private boolean spamInfricted(){
        if(type == SourceType.CHAT){
            String prelastMessage = infractionPlayer.preLastMessage();
            String lastMessage = infractionPlayer.lastMessage();

            return prelastMessage.equalsIgnoreCase(lastMessage) && lastMessage.contains(string);
        } else {
            String prelastCommand = infractionPlayer.preLastCommand();
            String lastCommand = infractionPlayer.lastCommand();

            return prelastCommand.equalsIgnoreCase(lastCommand) && lastCommand.contains(string);
        }
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SPAM;
    }
}
