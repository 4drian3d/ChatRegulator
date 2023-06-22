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

public final class PlayerManagerImpl implements PlayerManager {
    private final Cache<UUID, InfractionPlayerImpl> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();
    @Inject
    private Injector injector;
    @Inject
    private ProxyServer proxyServer;

    @Override
    public @NotNull InfractionPlayerImpl getPlayer(final @NotNull UUID uuid) {
        InfractionPlayerImpl infractionPlayer = infractionPlayers.getIfPresent(uuid);
        if (infractionPlayer != null) {
            return infractionPlayer;
        }
        final Player player = proxyServer.getPlayer(uuid).orElseThrow();
        infractionPlayers.put(uuid, infractionPlayer = new InfractionPlayerImpl(player, injector));

        return infractionPlayer;
    }

    public InfractionPlayerImpl getPlayer(final Player player) {
        return infractionPlayers.get(player.getUniqueId(), (id) -> new InfractionPlayerImpl(player, injector));
    }

    public void removePlayer(final UUID uuid) {
        infractionPlayers.invalidate(uuid);
    }

    public Collection<InfractionPlayerImpl> getPlayers() {
        return infractionPlayers.asMap().values();
    }
}
