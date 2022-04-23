package me.dreamerzero.chatregulator.listener.plugin;

import java.nio.file.Path;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;

/**Proxy reload listener */
@Internal
public final class ReloadListener {
    private final Path path;
    private final Logger logger;
    public ReloadListener(Path path, Logger logger){
        this.path = path;
        this.logger = logger;
    }

    /**
     * On proxy reload
     * @param event the event
     */
    @Subscribe
    public void onReload(ProxyReloadEvent event){
        Configuration.loadConfig(path, logger);
        FloodCheck.setFloodRegex();
    }
}
