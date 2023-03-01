package io.github._4drian3d.chatregulator.api.checks;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.Result;

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
    public @NotNull CompletableFuture<Result> check(final @NotNull String string){
        return CompletableFuture.supplyAsync(
            () -> new Result(string, this.spamInfricted(Objects.requireNonNull(string)))
        );
    }

    /**
     * Check if the {@link InfractionPlayer} is spamming
     * based on his 3 previous messages/commands.
     * If the {@link InfractionPlayer} is spamming, it will set detected as true
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
     * @param player the infractionPlayer involved
     * @param type the source type
     * @param string the string
     */
    public static @NotNull CompletableFuture<Result> createCheck(InfractionPlayer player, String string, SourceType type){
        return new SpamCheck(player, type).check(string);
    }
}
