package io.github._4drian3d.chatregulator.plugin.listener.list;

import com.google.inject.Inject;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.util.UUID;

public final class LeaveListener implements AwaitingEventExecutor<DisconnectEvent> {
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

    //PostOrder.LAST

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
                    configuration.getGeneralConfig().deleteUsersTime(),
                    configuration.getGeneralConfig().unit()
            ).schedule();
        });
    }
}
