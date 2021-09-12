package net.dreamerzero.chatregulator;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.listener.ChatListener;
import net.dreamerzero.chatregulator.listener.CommandListener;

public class Regulator {
    private final ProxyServer server;
    private final Logger logger;
    static Yaml config = new Yaml("config", "plugins/chatregulator");
    static Yaml blacklist = new Yaml("blacklist", "plugins/chatregulator");

    @Inject
    public Regulator(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        logger.info("ChatRegulator has started, have a very nice day.");
        // Default config
        Configuration.setDefaultConfig();
        // Register the PostLogin listener
        server.getEventManager().register(this, new ChatListener(server, logger));
        server.getEventManager().register(this, new CommandListener(server, logger));
    }
    public static Yaml getConfig(){
        return config;
    }
    public static Yaml getBlackList(){
        return blacklist;
    }
}