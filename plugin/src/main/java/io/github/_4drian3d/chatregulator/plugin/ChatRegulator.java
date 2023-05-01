package io.github._4drian3d.chatregulator.plugin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.ChatRegulatorAPI;
import io.github._4drian3d.chatregulator.plugin.commands.RegulatorCommand;
import io.github._4drian3d.chatregulator.plugin.config.Blacklist;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.Loader;
import io.github._4drian3d.chatregulator.plugin.config.Messages;
import io.github._4drian3d.chatregulator.plugin.listener.chat.ChatListener;
import io.github._4drian3d.chatregulator.plugin.listener.command.CommandListener;
import io.github._4drian3d.chatregulator.plugin.listener.command.SpyListener;
import io.github._4drian3d.chatregulator.plugin.listener.list.JoinListener;
import io.github._4drian3d.chatregulator.plugin.listener.list.LeaveListener;
import io.github._4drian3d.chatregulator.api.checks.FloodCheck;
import io.github._4drian3d.chatregulator.plugin.placeholders.RegulatorExpansion;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.MiniPlaceholderFormatter;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.NormalFormatter;
import io.github._4drian3d.chatregulator.plugin.source.RegulatorCommandSource;
import io.github._4drian3d.chatregulator.plugin.utils.Constants;
import net.byteflux.libby.Library;
import net.byteflux.libby.VelocityLibraryManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

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
public class ChatRegulator implements ChatRegulatorAPI {
    @Inject
    private ProxyServer server;
    @Inject
    private Logger logger;
    @Inject
    @DataDirectory
    private Path path;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private EventManager eventManager;
    @Inject
    private Injector injector;
    private CommandSource source;
    private IFormatter formatter;
    private StatisticsImpl statistics;
    private Configuration configuration;
    private Messages messages;
    private Blacklist blacklist;

    protected static final Cache<UUID, InfractionPlayerImpl> infractionPlayers = Caffeine.newBuilder()
            .weakKeys().build();

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.source = new RegulatorCommandSource(server.getEventManager(), server.getConsoleCommandSource());
        source.sendMessage(
            miniMessage().deserialize(
                    "<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>Starting plugin...")
        );
        this.loadDependencies();
        if (!this.reloadConfig()) {
            return;
        }

        this.statistics = new StatisticsImpl();

        if (server.getPluginManager().isLoaded("miniplaceholders")){
            this.formatter = new MiniPlaceholderFormatter();
            RegulatorExpansion.getExpansion(getPlayerManager()).register();
        } else {
            this.formatter = new NormalFormatter();
        }

        Stream.of(
                ChatListener.class,
                CommandListener.class,
                JoinListener.class,
                LeaveListener.class,
                SpyListener.class
        ).map(injector::getInstance)
       .forEach(listener -> eventManager.register(this, listener));

        eventManager.register(this, ProxyReloadEvent.class, e -> reloadConfig());
        injector.getInstance(RegulatorCommand.class).registerCommand();

        source.sendMessage(
            miniMessage().deserialize(
                "<gradient:#f2709c:#ff9472>ChatRegulator</gradient> <gradient:#DAE2F8:#D4D3DD>has started, have a very nice day</gradient>"
            )
        );
    }

    public @NotNull Logger getLogger(){
        return this.logger;
    }

    public @NotNull ProxyServer getProxy(){
        return this.server;
    }

    public IFormatter getFormatter(){
        return this.formatter;
    }

    public Map<UUID, InfractionPlayerImpl> getChatPlayers(){
        return infractionPlayers.asMap();
    }

    @Override
    public StatisticsImpl getStatistics() {
        return this.statistics;
    }

    public Configuration getConfig(){
        return this.configuration;
    }

    public Blacklist getBlacklist(){
        return this.blacklist;
    }

    public Messages getMessages(){
        return this.messages;
    }

    public CommandSource source() {
        return this.source;
    }

    public boolean reloadConfig(){
        Configuration config = Loader.loadMainConfig(path, logger);
        Messages msg = Loader.loadMessagesConfig(path, logger);
        Blacklist bl = Loader.loadBlacklistConfig(path, logger);

        if (config != null) {
            this.configuration = config;
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

    @Override
    public PlayerManagerImpl getPlayerManager() {
        return null;
    }
}