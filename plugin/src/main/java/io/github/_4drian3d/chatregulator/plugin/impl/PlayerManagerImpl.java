package io.github._4drian3d.chatregulator.plugin.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.PlayerManager;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class PlayerManagerImpl implements PlayerManager {
    private final Cache<UUID, InfractionPlayerImpl> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();
    @Inject
    private Logger logger;
    @Inject
    private FileLogger fileLogger;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private ConfigurationContainer<Checks> checksContainer;

    @Override
    public @NotNull InfractionPlayerImpl getPlayer(final @NotNull UUID uuid) {
        InfractionPlayerImpl infractionPlayer = infractionPlayers.getIfPresent(uuid);
        if (infractionPlayer != null) {
            return infractionPlayer;
        }
        infractionPlayers.put(uuid, infractionPlayer = new InfractionPlayerImpl(
                uuid, (playerUUID) -> proxyServer.getPlayer(playerUUID).orElse(null),
                proxyServer, checksContainer, logger, fileLogger
        ));

        return infractionPlayer;
    }

    public void removePlayer(final UUID uuid) {
        infractionPlayers.invalidate(uuid);
    }

    public Collection<InfractionPlayerImpl> getPlayers() {
        return infractionPlayers.asMap().values();
    }

    public Iterator<Map.Entry<UUID, InfractionPlayerImpl>> iterator() {
        return infractionPlayers.asMap().entrySet().iterator();
    }
}
