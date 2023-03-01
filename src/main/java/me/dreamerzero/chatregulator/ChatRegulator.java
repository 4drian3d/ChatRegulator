package me.dreamerzero.chatregulator;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import me.dreamerzero.chatregulator.commands.BrigadierRegulator;
import me.dreamerzero.chatregulator.config.Blacklist;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Loader;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.listener.chat.ChatListener;
import me.dreamerzero.chatregulator.listener.command.CommandListener;
import me.dreamerzero.chatregulator.listener.command.SpyListener;
import me.dreamerzero.chatregulator.listener.list.JoinListener;
import me.dreamerzero.chatregulator.listener.list.LeaveListener;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.utils.Constants;
import net.byteflux.libby.Library;
import net.byteflux.libby.VelocityLibraryManager;
import me.dreamerzero.chatregulator.placeholders.Placeholders;
import me.dreamerzero.chatregulator.placeholders.RegulatorExpansion;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import me.dreamerzero.chatregulator.placeholders.formatter.MiniPlaceholderFormatter;
import me.dreamerzero.chatregulator.placeholders.formatter.NormalFormatter;
import me.dreamerzero.chatregulator.source.RegulatorCommandSource;

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
    dependencies = {
        @Dependency(
            id = "miniplaceholders",
            optional = true
        )
    }
)
public class ChatRegulator {
    @Inject
    private ProxyServer server;
    @Inject
    private Logger logger;
    @Inject
    @DataDirectory
    private Path path;
    @Inject
    private PluginManager pluginManager;
    private CommandSource source;
    private IFormatter formatter;
    private Statistics statistics;
    private Placeholders placeholders;
    private Configuration configuration;
    private Messages messages;
    private Blacklist blacklist;

    /**
     * InfractionPlayer list
     */
    protected static final Cache<UUID, InfractionPlayer> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();

    /**
     * Initialization of the plugin
     * @param event the Initialize Event
     */
    @Subscribe
    @Internal
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.source = new RegulatorCommandSource(server.getEventManager(), server.getConsoleCommandSource());
        source.sendMessage(
            Components.MESSAGE_MINIMESSAGE
                .deserialize(
                    "<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>Starting plugin...")
        );
        this.loadDependencies();
        if (!this.reloadConfig()) {
            return;
        }

        this.statistics = new Statistics();
        this.placeholders = new Placeholders(this);

        if (server.getPluginManager().isLoaded("miniplaceholders")){
            this.formatter = new MiniPlaceholderFormatter();
            RegulatorExpansion.getExpansion().register();
        } else {
            this.formatter = new NormalFormatter();
        }

        this.registerListener(
            new ChatListener(this),
            new CommandListener(this),
            new JoinListener(),
            new LeaveListener(this),
            new SpyListener(this)
        );
        server.getEventManager().register(this, ProxyReloadEvent.class, e -> reloadConfig());
        BrigadierRegulator.registerCommand(this);

        source.sendMessage(
            Components.MESSAGE_MINIMESSAGE
                .deserialize(
                    "<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"
                )
        );
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

    public Map<UUID, InfractionPlayer> getChatPlayers(){
        return infractionPlayers.asMap();
    }

    public Statistics getStatistics() {
        return this.statistics;
    }

    public Placeholders placeholders() {
        return this.placeholders;
    }

    /**
     * Get the main Configuration
     * @return the general configuration
     */
    public Configuration getConfig(){
        return this.configuration;
    }

    /**
     * Get the Blacklist configuration
     * @return the Blacklist configuration
     */
    public Blacklist getBlacklist(){
        return this.blacklist;
    }

    /**
     * Get the Messages Configuration
     * @return the Messages configuration
     */
    public Messages getMessages(){
        return this.messages;
    }

    public CommandSource source() {
        return this.source;
    }

    /**
     * Reload the plugin configuration
     */
    public boolean reloadConfig(){
        Configuration config = Loader.loadMainConfig(path, logger);
        Messages msg = Loader.loadMessagesConfig(path, logger);
        Blacklist bl = Loader.loadBlacklistConfig(path, logger);

        if (config != null) {
            this.configuration = config;
            FloodCheck.setFloodRegex(config.getFloodConfig().getLimit());
        }

        if (msg != null) {
            this.messages = msg;
        }

        if (bl != null) {
            this.blacklist = bl;
        }

        return !(bl == null || config == null || msg == null);
    }

    private void loadDependencies() {
        final var libraryManager
                = new VelocityLibraryManager<>(logger, this.path, pluginManager, this, "libs");

        final Library hocon = Library.builder()
            .groupId("org{}spongepowered")
            .artifactId("configurate-hocon")
            .version(Constants.CONFIGURATE)
            .id("configurate-hocon")
            .build();
        final Library confCore = Library.builder()
            .groupId("org{}spongepowered")
            .artifactId("configurate-core")
            .version(Constants.CONFIGURATE)
            .id("configurate-core")
            .build();
        final Library geantyref = Library.builder()
            .groupId("io{}leangen{}geantyref")
            .artifactId("geantyref")
            .version(Constants.GEANTYREF)
            .id("geantyref")
            .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(hocon);
        libraryManager.loadLibrary(confCore);
        libraryManager.loadLibrary(geantyref);
    }

    public void removePlayer(UUID uuid) {
        infractionPlayers.invalidate(uuid);
    }
}