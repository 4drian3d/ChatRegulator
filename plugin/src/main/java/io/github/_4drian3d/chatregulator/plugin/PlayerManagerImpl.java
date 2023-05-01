package io.github._4drian3d.chatregulator.plugin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.PlayerManager;

import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {
    private final Cache<UUID, InfractionPlayerImpl> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();
    @Override
    public InfractionPlayerImpl getPlayer(UUID uuid) {
        return null;
    }

    public InfractionPlayerImpl getPlayer(Player player) {
        return null;
    }

    public InfractionPlayerImpl getPlayer(String name) {
        return null;
    }

    public void removePlayer(UUID uuid) {

    }
}
