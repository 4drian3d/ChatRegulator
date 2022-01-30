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
public class SpamCheck implements ICheck {
    private final InfractionPlayer infractionPlayer;
    private final SourceType type;
    //TODO: Add SpamTest tests
    SpamCheck(@NotNull InfractionPlayer infractionPlayer, @NotNull SourceType type){
        this.infractionPlayer = Objects.requireNonNull(infractionPlayer);
        this.type = Objects.requireNonNull(type);
    }

    /**
     * {@inheritDoc}
     * @return A {@link Result} of this check
     */
    @Override
    public CompletableFuture<Result> check(@NotNull String string){
        boolean infricted = this.spamInfricted(Objects.requireNonNull(string));
        return CompletableFuture.completedFuture(new Result(string, infricted));
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detectec as true
     */
    private boolean spamInfricted(String string){
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

    /**
     * Create a new spam check
     * @param player the infractionPlayer involucred
     * @param type the source type
     * @param string the string
     */
    public static CompletableFuture<Result> createCheck(InfractionPlayer player, String string, SourceType type){
        return new SpamCheck(player, type).check(string);
    }
}
