package net.dreamerzero.chatregulator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.commands.ChatRegulatorCommand;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.listener.chat.ChatListener;
import net.dreamerzero.chatregulator.listener.command.CommandListener;
import net.dreamerzero.chatregulator.listener.list.JoinListener;
import net.dreamerzero.chatregulator.listener.list.LeaveListener;
import net.dreamerzero.chatregulator.listener.plugin.PluginListener;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Plugin main class
 */
public class Regulator {
    private final ProxyServer server;
    private Yaml config;
    private Yaml blacklist;
    /**
     * InfractionPlayer list
     */
    protected static Map<UUID, InfractionPlayer> infractionPlayers = new HashMap<>();
    private final Logger logger;

    /**
     * Constructor for ChatRegulator Plugin
     * @param server the proxy server
     * @param logger logger
     */
    @Inject
    public Regulator(final ProxyServer server, Logger logger) {
        this.server = server;
        this.config = new Yaml("config", "plugins/ChatRegulator");
        this.blacklist = new Yaml("blacklist", "plugins/ChatRegulator");
        this.logger = logger;
    }

    @Subscribe
    /**
     * Initialization of the plugin
     */
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        server.getConsoleCommandSource().sendMessage(
            MiniMessage.miniMessage().parse("<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"));
        // Default config
        new Configuration(config, blacklist).setDefaultConfig();

        server.getEventManager().register(this, new PluginListener(logger));
        server.getEventManager().register(this, new ChatListener(server, logger, config, blacklist));
        server.getEventManager().register(this, new CommandListener(server, logger, config, blacklist));
        server.getEventManager().register(this, new JoinListener(infractionPlayers));
        server.getEventManager().register(this, new LeaveListener());

        CommandMeta regulatorMeta = server.getCommandManager().metaBuilder("chatregulator").aliases("chatr", "cregulator").build();
        server.getCommandManager().register(regulatorMeta, new ChatRegulatorCommand(infractionPlayers, config, server));
    }
    /**
     * Get the plugin configuration
     * @return the plugin configuration
     */
    public Yaml getConfig(){
        return this.config;
    }
    /**
     * Get the configuration of the blacklist of banned words
     * @return the blacklist configuration
     */
    public Yaml getBlackList(){
        return this.blacklist;
    }
}