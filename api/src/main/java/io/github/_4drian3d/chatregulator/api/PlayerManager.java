package io.github._4drian3d.chatregulator.api;

import java.util.UUID;

public interface PlayerManager {
    InfractionPlayer getPlayer(UUID uuid);
}
