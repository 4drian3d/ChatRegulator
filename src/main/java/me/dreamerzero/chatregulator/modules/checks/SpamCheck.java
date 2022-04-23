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
public final class SpamCheck implements ICheck {
    private final InfractionPlayer infractionPlayer;
    private final SourceType type;

    private SpamCheck(@NotNull InfractionPlayer infractionPlayer, @NotNull SourceType type){
        this.infractionPlayer = Objects.requireNonNull(infractionPlayer);
        this.type = Objects.requireNonNull(type);
    }

    /**
     * {@inheritDoc}
     * @return A {@link Result} of this check
     */
    @Override
    public CompletableFuture<Result> check(final @NotNull String string){
        final boolean infricted = this.spamInfricted(Objects.requireNonNull(string));
        return CompletableFuture.completedFuture(new Result(string, infricted));
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detectec as true
     */
    private boolean spamInfricted(final String string){
        return type == SourceType.CHAT
            ? this.spamCheck(infractionPlayer.preLastMessage(), infractionPlayer.lastMessage(), string)
            : this.spamCheck(infractionPlayer.preLastCommand(), infractionPlayer.lastCommand(), string);
    }

    private boolean spamCheck(final String pre, final String last, final String string){
        return pre.equalsIgnoreCase(last) && last.contains(string);
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
    public static @NotNull CompletableFuture<Result> createCheck(InfractionPlayer player, String string, SourceType type){
        return new SpamCheck(player, type).check(string);
    }
}
