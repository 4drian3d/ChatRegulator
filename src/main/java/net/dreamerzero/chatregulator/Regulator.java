package net.dreamerzero.chatregulator;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.commands.ChatRegulatorCommand;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.listener.chat.ChatListener;
import net.dreamerzero.chatregulator.listener.command.CommandListener;
import net.dreamerzero.chatregulator.listener.list.JoinListener;
import net.dreamerzero.chatregulator.listener.list.LeaveListener;
import net.dreamerzero.chatregulator.listener.plugin.PluginListener;
import net.dreamerzero.chatregulator.listener.plugin.ReloadListener;
import net.dreamerzero.chatregulator.utils.Constants;
import net.kyori.adventure.text.minimessage.MiniMessage;

/**
 * Plugin main class
 */
@Plugin(
    id = "chatregulator",
    name = Constants.NAME,
    version = Constants.VERSION,
    description = Constants.DESCRIPTION,
    url = Constants.URL,
    authors = {
        "4drian3d"
    },
    // The dependency is necessary only to send a warning message when reloading the plugin.
    dependencies = {
        @Dependency(
            id = "serverutils",
            optional = true
        )
    }
)
public class Regulator {
    private final ProxyServer server;
    private final Logger logger;
    private static Regulator plugin;
    private Path path;

    /**
     * InfractionPlayer list
     */
    protected static Map<UUID, InfractionPlayer> infractionPlayers = new ConcurrentHashMap<>();

    /**
     * Constructor for ChatRegulator Plugin
     * @param server the proxy server
     * @param logger logger
     */
    @Inject
    public Regulator(final ProxyServer server, Logger logger, @DataDirectory Path path) {
        this.server = server;
        this.path = path;
        this.logger = logger;
    }

    @Subscribe
    /**
     * Initialization of the plugin
     */
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        Regulator.plugin = this;
        // :)
        server.getConsoleCommandSource().sendMessage(
            MiniMessage.miniMessage().parse("<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"));
        // Default config
        Configuration.loadConfig(path, logger);
        if(server.getPluginManager().isLoaded("ServerUtils")){
            server.getEventManager().register(this, new PluginListener(logger));
        }
        server.getEventManager().register(this, new ChatListener(server));
        server.getEventManager().register(this, new CommandListener(server));
        server.getEventManager().register(this, new JoinListener(infractionPlayers));
        server.getEventManager().register(this, new LeaveListener());
        server.getEventManager().register(this, new ReloadListener(path, logger));

        CommandMeta regulatorMeta = server.getCommandManager().metaBuilder("chatregulator").aliases("chatr", "cregulator").build();
        server.getCommandManager().register(regulatorMeta, new ChatRegulatorCommand(infractionPlayers, server));

        checkInfractionPlayersRunnable();
    }

    public static Regulator getInstance(){
        return plugin;
    }

    public Logger getLogger(){
        return this.logger;
    }

    public ProxyServer getProxy(){
        return this.server;
    }

    /**
     * Verification check for players who have
     * left the server and have not re-entered
     * in the configured time
     */
    void checkInfractionPlayersRunnable(){
        long timeToDelete = Configuration.getConfig().getGeneralConfig().deleteUsersTime()*1000;
        server.getScheduler().buildTask(this, ()->{
            for(Entry<UUID, InfractionPlayer> entry : infractionPlayers.entrySet()){
                InfractionPlayer iPlayer = entry.getValue();
                if(iPlayer.isOnline()) continue;
                if(iPlayer.getLastSeen() - System.currentTimeMillis() > timeToDelete){
                    infractionPlayers.remove(entry.getKey());
                    logger.debug("The player {} was eliminated", iPlayer.username());
                }
            }
        })
        .repeat(timeToDelete, TimeUnit.MILLISECONDS)
        .schedule();
    }

    public void reloadConfig(){
        Configuration.loadConfig(path, logger);
    }
}