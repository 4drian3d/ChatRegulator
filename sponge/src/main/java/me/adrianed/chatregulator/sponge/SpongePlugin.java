package me.adrianed.chatregulator.sponge;

import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import me.adrianed.chatregulator.api.RegulatorPlugin;
import me.adrianed.chatregulator.api.configuration.Blacklist;
import me.adrianed.chatregulator.api.configuration.Configuration;
import me.adrianed.chatregulator.api.configuration.Messages;
import me.adrianed.chatregulator.api.event.EventManager;
import me.adrianed.chatregulator.api.logger.Log4JLogger;
import me.adrianed.chatregulator.api.logger.RegulatorLogger;

import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

@Plugin("chatregulator")
public class SpongePlugin implements RegulatorPlugin {
    private final RegulatorLogger logger;
    private final Path path;

    @Inject
    public SpongePlugin(Logger logger, Path path) {
        //TODO: aaaaaaaa 3 different loggers???
        this.logger = new Log4JLogger();
        this.path = path;
    }

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        logger.info("ChatRegulator Sponge");
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

    @Override
    public Blacklist blacklist() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EventManager eventBus() {
        // TODO Auto-generated method stub
        return null;
    }
}
