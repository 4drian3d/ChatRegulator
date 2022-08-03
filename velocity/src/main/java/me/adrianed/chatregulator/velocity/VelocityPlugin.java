package me.adrianed.chatregulator.velocity;

import java.nio.file.Path;

import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

public class VelocityPlugin {
    private final Path path;
    private final ProxyServer proxy;
    private final EventManager eventManager;

    public VelocityPlugin(@DataDirectory Path path, ProxyServer proxy, EventManager eventManager) {
        this.path = path;
        this.proxy = proxy;
        this.eventManager = eventManager;
    }
}