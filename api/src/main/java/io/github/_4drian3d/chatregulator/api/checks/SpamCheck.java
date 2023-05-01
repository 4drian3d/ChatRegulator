package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Detection of command/message spamming
 */
public final class SpamCheck implements ICheck {
    private final SourceType type;

    private SpamCheck(final @NotNull SourceType type){
        this.type = Objects.requireNonNull(type);
    }

    private boolean spamCheck(final String pre, final String last, final String string){
        return pre.equalsIgnoreCase(last) && last.contains(string);
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final boolean infraction = type == SourceType.CHAT
                ? this.spamCheck(player.preLastMessage(), player.lastMessage(), string)
                : this.spamCheck(player.preLastCommand(), player.lastCommand(), string);
        return infraction
                ? CheckResult.denied()
                : CheckResult.allowed();
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.SPAM;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements AbstractBuilder<SpamCheck> {
        private SourceType source;

        Builder() {}

        public Builder source(SourceType source){
            this.source = source;
            return this;
        }

        @Override
        public @NotNull SpamCheck build(){
            requireNonNull(source);
            return new SpamCheck(source);
        }
    }
}
