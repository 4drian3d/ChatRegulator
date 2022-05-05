package me.dreamerzero.chatregulator;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
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
import me.dreamerzero.chatregulator.listener.plugin.ReloadListener;
import me.dreamerzero.chatregulator.utils.Constants;
import net.byteflux.libby.Library;
import net.byteflux.libby.VelocityLibraryManager;
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
    dependencies = {
        @Dependency(
            id = "miniplaceholders",
            optional = true
        )
    }
)
public class ChatRegulator {
    private final ProxyServer server;
    private final Logger logger;
    private final Path path;
    private final PluginManager manager;
    private IFormatter formatter;

    /**
     * InfractionPlayer list
     */
    protected static final Cache<UUID, InfractionPlayer> infractionPlayers = Caffeine.newBuilder().weakKeys().build();

    /**
     * Constructor for ChatRegulator Plugin
     * @param server the proxy server
     * @param logger logger
     * @param path the plugin path
     */
    @Inject
    @Internal
    public ChatRegulator(
        final ProxyServer server,
        final Logger logger,
        final @DataDirectory Path path,
        final PluginManager pmanager
    ) {
        this.server = server;
        this.path = path;
        this.logger = logger;
        this.manager = pmanager;
    }

    /**
     * Initialization of the plugin
     * @param event the Initialize Event
     */
    @Subscribe
    @Internal
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        server.getConsoleCommandSource().sendMessage(
            Components.MESSAGE_MINIMESSAGE
                .deserialize(
                    "<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>Starting plugin...")
        );
        this.loadDependencies();
        Configuration.loadConfig(path, logger);

        if(server.getPluginManager().isLoaded("miniplaceholders")){
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
            new ReloadListener(path, logger),
            new SpyListener(this)
        );
        BrigadierRegulator.registerCommand(this);

        server.getConsoleCommandSource().sendMessage(
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

    /**
     * Reload the plugin configuration
     */
    public void reloadConfig(){
        Configuration.loadConfig(path, logger);
    }

    private void loadDependencies() {
        final VelocityLibraryManager<ChatRegulator> libraryManager = new VelocityLibraryManager<>(logger, this.path, manager, this, "libs");

        final Library hocon = Library.builder()
            .groupId("org{}spongepowered")
            .artifactId("configurate-hocon")
            .version("4.1.2")
            .id("configurate-hocon")
            .build();
        final Library confCore = Library.builder()
            .groupId("org{}spongepowered")
            .artifactId("configurate-core")
            .version("4.1.2")
            .id("configurate-core")
            .build();
        final Library geantyref = Library.builder()
            .groupId("io{}leangen{}geantyref")
            .artifactId("geantyref")
            .version("1.3.13")
            .id("geantyref")
            .build();
        final Library caffeine = Library.builder()
            .groupId("com{}github{}ben-manes{}caffeine")
            .artifactId("caffeine")
            .version("3.0.6")
            .id("caffeine")
            .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(hocon);
        libraryManager.loadLibrary(confCore);
        libraryManager.loadLibrary(geantyref);
        libraryManager.loadLibrary(caffeine);
    }

    public void removePlayer(UUID uuid) {
        infractionPlayers.invalidate(uuid);
    }
}