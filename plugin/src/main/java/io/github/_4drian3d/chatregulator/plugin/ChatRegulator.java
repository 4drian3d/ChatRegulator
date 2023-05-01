package io.github._4drian3d.chatregulator.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import io.github._4drian3d.chatregulator.api.ChatRegulatorAPI;
import io.github._4drian3d.chatregulator.plugin.commands.RegulatorCommand;
import io.github._4drian3d.chatregulator.plugin.listener.chat.ChatListener;
import io.github._4drian3d.chatregulator.plugin.listener.command.CommandListener;
import io.github._4drian3d.chatregulator.plugin.listener.command.SpyListener;
import io.github._4drian3d.chatregulator.plugin.listener.list.JoinListener;
import io.github._4drian3d.chatregulator.plugin.listener.list.LeaveListener;
import io.github._4drian3d.chatregulator.plugin.modules.ConfigurationModule;
import io.github._4drian3d.chatregulator.plugin.modules.PluginModule;
import io.github._4drian3d.chatregulator.plugin.modules.ProviderModule;
import io.github._4drian3d.chatregulator.plugin.utils.Constants;
import io.github._4drian3d.velocityhexlogger.HexLogger;

import java.util.stream.Stream;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@Plugin(
        id = "chatregulator",
        name = "ChatRegulator",
        version = Constants.VERSION,
        description = Constants.DESCRIPTION,
        url = "https://modrinth.com/plugin/chatregulator",
        authors = {
                "4drian3d"
        },
        dependencies = {
                @Dependency(
                        id = "miniplaceholders",
                        optional = true
                ),
                @Dependency(
                        id = "unsignedvelocity",
                        optional = true
                )
        }
)
public class ChatRegulator implements ChatRegulatorAPI {
    @Inject
    private EventManager eventManager;
    @Inject
    private HexLogger logger;
    @Inject
    private Injector injector;
    private final StatisticsImpl statistics = new StatisticsImpl();
    private final PlayerManagerImpl playerManager = new PlayerManagerImpl();

    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        this.injector = injector.createChildInjector(
                new ConfigurationModule(),
                new ProviderModule(),
                new PluginModule()
        );
        logger.info(miniMessage().deserialize("<gradient:#DAE2F8:#D4D3DD>Starting plugin..."));

        Stream.of(
                        ChatListener.class,
                        CommandListener.class,
                        JoinListener.class,
                        LeaveListener.class,
                        SpyListener.class
                ).map(injector::getInstance)
                .forEach(listener -> eventManager.register(this, listener));

        injector.getInstance(RegulatorCommand.class).registerCommand();

        logger.info(miniMessage().deserialize("<gradient:#DAE2F8:#D4D3DD>has been started"));
    }

    @Override
    public StatisticsImpl getStatistics() {
        return this.statistics;
    }

    @Override
    public PlayerManagerImpl getPlayerManager() {
        return this.playerManager;
    }
}