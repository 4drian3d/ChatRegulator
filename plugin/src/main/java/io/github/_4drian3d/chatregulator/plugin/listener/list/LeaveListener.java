package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.util.UUID;

public final class LeaveListener implements RegulatorExecutor<DisconnectEvent> {
    @Inject
    private ChatRegulator plugin;
    @Inject
    private PlayerManagerImpl playerManager;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private Logger logger;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;

    @Override
    public @Nullable EventTask executeAsync(DisconnectEvent event) {
        if (event.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
            return null;
        }
        return EventTask.async(() -> {
            final InfractionPlayerImpl player = playerManager.getPlayer(event.getPlayer());
            player.isOnline(false);

            final UUID uuid = event.getPlayer().getUniqueId();
            final Configuration configuration = configurationContainer.get();

            proxyServer.getScheduler().buildTask(plugin, () -> {
                if (proxyServer.getPlayer(uuid).isEmpty()) {
                    logger.debug("The player {} was removed", player.username());
                    playerManager.removePlayer(uuid);
                }
            }).delay(
                    configuration.deleteUsersTime(),
                    configuration.unit()
            ).schedule();
        });
    }

    @Override
    public Class<DisconnectEvent> eventClass() {
        return DisconnectEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.LAST;
    }
}
