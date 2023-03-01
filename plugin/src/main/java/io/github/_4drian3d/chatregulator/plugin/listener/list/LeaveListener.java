package io.github._4drian3d.chatregulator.plugin.listener.list;

import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.jetbrains.annotations.ApiStatus.Internal;

import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import org.slf4j.Logger;

@Internal
public final class LeaveListener {
    @Inject
    private ChatRegulator plugin;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private Logger logger;

    /**
     * In case the player leaves the server,
     * his online status is marked as false.
     * @param event the event
     */
    @Subscribe(order = PostOrder.LAST)
    public EventTask onLeave(final DisconnectEvent event){
        if (event.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN) {
            return null;
        }
        return EventTask.async(() -> {
            final InfractionPlayerImpl player = plugin.getPlayerManager().onQuit(event.getPlayer());
            player.isOnline(false);

            final UUID uuid = event.getPlayer().getUniqueId();

            proxyServer.getScheduler().buildTask(plugin, () -> {
                if (proxyServer.getPlayer(uuid).isEmpty()) {
                    logger.debug("The player {} was removed", player.username());
                    plugin.removePlayer(uuid);
                }
            }).delay(
                plugin.getConfig().getGeneralConfig().deleteUsersTime(),
                plugin.getConfig().getGeneralConfig().unit()
            ).schedule();
        });
    }
}
