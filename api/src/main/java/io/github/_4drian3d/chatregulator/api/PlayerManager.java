package io.github._4drian3d.chatregulator.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerManager {
    InfractionPlayer getPlayer(final @NotNull UUID uuid);
}
