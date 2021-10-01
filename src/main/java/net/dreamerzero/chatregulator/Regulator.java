package net.dreamerzero.chatregulator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;

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
    private static ProxyServer proxy;
    static Yaml config = new Yaml("config", "plugins/chatregulator");
    static Yaml blacklist = new Yaml("blacklist", "plugins/chatregulator");
    private static Map<UUID, InfractionPlayer> players;

    @Inject
    public Regulator(final ProxyServer server) {
        this.server = server;
        proxy = server;
        players = new HashMap<>();
    }

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        // :)
        server.getConsoleCommandSource().sendMessage(
            MiniMessage.miniMessage().parse("<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"));
        // Default config
        Configuration.setDefaultConfig();
        // Register the PostLogin listener
        server.getEventManager().register(this, new ChatListener(server));
        server.getEventManager().register(this, new CommandListener(server));
        server.getEventManager().register(this, new JoinListener());
        server.getEventManager().register(this, new LeaveListener());
    }
    /**
     * Get the plugin configuration
     * @return the plugin configuration
     */
    public static Yaml getConfig(){
        return config;
    }
    /**
     * Get the configuration of the blacklist of banned words
     * @return the blacklist configuration
     */
    public static Yaml getBlackList(){
        return blacklist;
    }
    public static ProxyServer getProxyServer(){
        return proxy;
    }
    public static Map<UUID, InfractionPlayer> getInfractionPlayers(){
        return players;
    }
    /**
     * Get the InfractionPlayer based on a UUID
     * @param uuid the player uuid
     * @return the {@link InfractionPlayer}
     */
    public static InfractionPlayer getInfractionPlayer(UUID uuid){
        return players.containsKey(uuid) ? players.get(uuid) : null;
    }
}