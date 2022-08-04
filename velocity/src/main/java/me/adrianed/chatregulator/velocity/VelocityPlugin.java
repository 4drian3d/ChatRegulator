package me.adrianed.chatregulator.velocity;

import java.nio.file.Path;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import me.adrianed.chatregulator.api.RegulatorPlugin;
import me.adrianed.chatregulator.api.configuration.Configuration;
import me.adrianed.chatregulator.api.configuration.Messages;
import me.adrianed.chatregulator.api.logger.RegulatorLogger;
import me.adrianed.chatregulator.api.logger.SLF4JLogger;

@Plugin(
    id = "chatregulator",
    name = "ChatRegulator",
    authors = ("4drian3d"),
    version = "4.0.0-SNAPSHOT"
)
public class VelocityPlugin implements RegulatorPlugin {
    private final Path path;
    private final ProxyServer proxy;
    private final EventManager eventManager;
    private final SLF4JLogger logger;

    @Inject
    public VelocityPlugin(@DataDirectory Path path, ProxyServer proxy, EventManager eventManager, Logger logger) {
        this.path = path;
        this.proxy = proxy;
        this.eventManager = eventManager;
        this.logger = new SLF4JLogger(logger);
    }

    @Override
    public Path path() {
        return this.path;
    }

    @Override
    public RegulatorLogger logger() {
        return this.logger;
    }

    @Override
    public Configuration configuration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Messages messages() {
        // TODO Auto-generated method stub
        return null;
    }
}