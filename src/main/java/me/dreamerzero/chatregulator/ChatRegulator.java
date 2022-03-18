package me.dreamerzero.chatregulator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import me.dreamerzero.chatregulator.commands.BrigadierRegulator;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.listener.chat.ChatListener;
import me.dreamerzero.chatregulator.listener.command.CommandListener;
import me.dreamerzero.chatregulator.listener.command.SpyListener;
import me.dreamerzero.chatregulator.listener.list.JoinListener;
import me.dreamerzero.chatregulator.listener.list.LeaveListener;
import me.dreamerzero.chatregulator.listener.plugin.PluginListener;
import me.dreamerzero.chatregulator.listener.plugin.ReloadListener;
import me.dreamerzero.chatregulator.utils.Constants;
import me.dreamerzero.chatregulator.placeholders.RegulatorExpansion;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import me.dreamerzero.chatregulator.placeholders.formatter.MiniPlaceholderFormatter;
import me.dreamerzero.chatregulator.placeholders.formatter.NormalFormatter;

/**
 * Plugin main class
 */
@Plugin(
    id = Constants.ID,
    name = Constants.NAME,
    version = Constants.VERSION,
    description = Constants.DESCRIPTION,
    url = Constants.URL,
    authors = {
        "4drian3d"
    },
    // This dependency is necessary only to send a warning message when reloading the plugin.
    dependencies = {
        @Dependency(
            id = "serverutils",
            optional = true
        )
    }
)
public class ChatRegulator {
    private final ProxyServer server;
    private final Logger logger;
    private final Path path;
    private IFormatter formatter;

    /**
     * InfractionPlayer list
     */
    protected static final Map<UUID, InfractionPlayer> infractionPlayers = new ConcurrentHashMap<>();

    /**
     * Constructor for ChatRegulator Plugin
     * @param server the proxy server
     * @param logger logger
     * @param path the plugin path
     */
    @Inject
    @Internal
    public ChatRegulator(final ProxyServer server, Logger logger, @DataDirectory Path path) {
        this.server = server;
        this.path = path;
        this.logger = logger;
    }

    /**
     * Initialization of the plugin
     * @param event the Initialize Event
     */
    @Subscribe
    @Internal
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        server.getConsoleCommandSource().sendMessage(Components.MESSAGE_MINIMESSAGE
            .deserialize("<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"));
        Configuration.loadConfig(path, logger);

        if(server.getPluginManager().isLoaded("serverutils")){
            this.registerListener(new PluginListener(logger));
        }

        if(server.getPluginManager().isLoaded("miniplaceholders")){
            this.formatter = new MiniPlaceholderFormatter();
            RegulatorExpansion.getExpansion().register();
        } else {
            this.formatter = new NormalFormatter();
        }

        this.registerListener(
            new ChatListener(this),
            new CommandListener(this),
            new JoinListener(infractionPlayers),
            new LeaveListener(),
            new ReloadListener(path, logger),
            new SpyListener(this)
        );
        BrigadierRegulator.registerCommand(this);
        checkInfractionPlayersRunnable();
    }

    private void registerListener(@NotNull Object @NotNull... events){
        for(Object event : events){
            server.getEventManager().register(this, event);
        }
    }

    /**
     * Get the plugin logger
     * @return the plugin logger
     */
    @Internal
    public @NotNull Logger getLogger(){
        return this.logger;
    }

    /**
     * Get the proxy
     * @return the proxy server
     */
    @Internal
    public @NotNull ProxyServer getProxy(){
        return this.server;
    }

    public IFormatter getFormatter(){
        return this.formatter;
    }

    public Collection<InfractionPlayer> getChatPlayers(){
        return Set.copyOf(infractionPlayers.values());
    }

    /**
     * Verification check for players who have
     * left the server and have not re-entered
     * in the configured time
     */
    private void checkInfractionPlayersRunnable(){
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

    /**
     * Reload the plugin configuration
     */
    public void reloadConfig(){
        Configuration.loadConfig(path, logger);
    }
}