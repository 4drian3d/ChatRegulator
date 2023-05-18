package io.github._4drian3d.chatregulator.plugin.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {
    private final Cache<UUID, InfractionPlayerImpl> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();
    @Inject
    private Injector injector;
    @Inject
    private ProxyServer proxyServer;

    @Override
    public InfractionPlayerImpl getPlayer(@NotNull UUID uuid) {
        return infractionPlayers.get(uuid, (id) -> new InfractionPlayerImpl(proxyServer.getPlayer(uuid).orElseThrow(), injector));
    }

    public InfractionPlayerImpl getPlayer(Player player) {
        return infractionPlayers.get(player.getUniqueId(), (id) -> new InfractionPlayerImpl(player, injector));
    }

    public void removePlayer(UUID uuid) {
        infractionPlayers.invalidate(uuid);
    }

    public Collection<InfractionPlayerImpl> getPlayers() {
        return infractionPlayers.asMap().values();
    }
}
