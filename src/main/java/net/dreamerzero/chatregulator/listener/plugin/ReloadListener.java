package net.dreamerzero.chatregulator.listener.plugin;

import java.nio.file.Path;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;

public class ReloadListener {
    private final Path path;
    private final Logger logger;
    public ReloadListener(Path path, Logger logger){
        this.path = path;
        this.logger = logger;
    }
    @Subscribe
    public void onReload(ProxyReloadEvent event){
        Configuration.loadConfig(path, logger);
        FloodCheck.setFloodRegex();
    }
}
