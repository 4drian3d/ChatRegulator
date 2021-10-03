package net.dreamerzero.chatregulator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.listener.ChatListener;
import net.dreamerzero.chatregulator.listener.CommandListener;
import net.dreamerzero.chatregulator.listener.JoinListener;
import net.dreamerzero.chatregulator.listener.LeaveListener;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Regulator {
    private final ProxyServer server;
    private Yaml config;
    private Yaml blacklist;
    private Map<UUID, InfractionPlayer> infractionPlayers;
    private final Logger logger;

    @Inject
    public Regulator(final ProxyServer server, Logger logger) {
        this.server = server;
        infractionPlayers = new HashMap<>();
        config = new Yaml("config", "plugins/chatregulator");
        blacklist = new Yaml("blacklist", "plugins/chatregulator");
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        server.getConsoleCommandSource().sendMessage(
            MiniMessage.miniMessage().parse("<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"));
        // Default config
        new Configuration(config, blacklist).setDefaultConfig();
        // Register the PostLogin listener
        server.getEventManager().register(this, new ChatListener(server, logger, config, blacklist, infractionPlayers));
        server.getEventManager().register(this, new CommandListener(server, logger, config, blacklist, infractionPlayers));
        server.getEventManager().register(this, new JoinListener(infractionPlayers));
        server.getEventManager().register(this, new LeaveListener(infractionPlayers));
    }
    /**
     * Get the plugin configuration
     * @return the plugin configuration
     */
    public Yaml getConfig(){
        return config;
    }
    /**
     * Get the configuration of the blacklist of banned words
     * @return the blacklist configuration
     */
    public Yaml getBlackList(){
        return blacklist;
    }

    /**
     * Get the InfractionPlayer based on a UUID
     * @param uuid the player uuid
     * @return the {@link InfractionPlayer}
     */
    public InfractionPlayer getInfractionPlayer(UUID uuid){
        return infractionPlayers.containsKey(uuid) ? infractionPlayers.get(uuid) : null;
    }
}