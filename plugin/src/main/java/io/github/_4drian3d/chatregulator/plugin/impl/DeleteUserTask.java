package io.github._4drian3d.chatregulator.plugin.impl;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;

import javax.inject.Singleton;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class DeleteUserTask {
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private ChatRegulator plugin;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private PlayerManagerImpl playerManager;
    private ScheduledTask task = null;

    public void start() {
        final Configuration configuration = configurationContainer.get();
        final long deleteUsersTime = configuration.deleteUsersTime();
        final TimeUnit deleteUsersTimeUnit = configuration.unit();
        task = proxyServer.getScheduler().buildTask(plugin, (task) -> {
            final Iterator<Map.Entry<UUID, InfractionPlayerImpl>> playerIterator = playerManager.iterator();
            Map.Entry<UUID, InfractionPlayerImpl> entry;
            while (playerIterator.hasNext()) {
                entry = playerIterator.next();
                if (!entry.getValue().isOnline()) {
                    playerIterator.remove();
                }
            }
            if (configurationContainer.get().deleteUsersTime() != deleteUsersTime) {
                this.restart();
            }
        }).repeat(deleteUsersTime, deleteUsersTimeUnit).schedule();
    }

    public void restart() {
        if (task == null) {
            return;
        }
        task.cancel();
        task = null;
        this.start();
    }
}
