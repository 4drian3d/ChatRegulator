package io.github._4drian3d.chatregulator.common.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public record PlayerWrapper(UUID playerUUID, Function<UUID, Audience> audienceProvider) implements ForwardingAudience.Single {
    @Override
    public @NotNull Audience audience() {
        final Audience audience = audienceProvider.apply(playerUUID);
        return audience == null ? Audience.empty() : audience;
    }

    public boolean isOnline() {
        return audienceProvider.apply(playerUUID) != null;
    }

    public @Nullable Audience getAudience() {
        return audienceProvider.apply(playerUUID);
    }
}
