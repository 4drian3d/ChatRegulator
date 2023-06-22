package io.github._4drian3d.chatregulator.plugin.listener.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.config.Messages;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class SpyListener implements RegulatorExecutor<CommandExecuteEvent> {
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private Formatter formatter;
    @Inject
    private ProxyServer proxyServer;

    @Override
    public EventTask executeAsync(final CommandExecuteEvent event) {
        return EventTask.async(() -> {
            final CommandSource source = event.getCommandSource();
            final Configuration.CommandSpy config = configurationContainer.get().getCommandSpyConfig();
            if (!event.getResult().isAllowed()
                    || !config.enabled()
                    || !(source instanceof final Player player)
                    || Permission.BYPASS_COMMAND_SPY.test(source)
            ) {
                return;
            }

            final String command = event.getCommand();
            if (config.shouldAnnounce(source, command)) {
                final Component message = formatter.parse(
                        messagesContainer.get().getCommandSpyMessages().getMessage(),
                        player,
                        TagResolver.resolver(
                                Placeholder.unparsed("command", command),
                                Placeholder.unparsed("player", player.getUsername())
                        )
                );
                proxyServer.getAllPlayers().forEach(pl -> {
                    if (Permission.COMMANDSPY_ALERT.test(pl)) {
                        pl.sendMessage(message);
                    }
                });
                proxyServer.getConsoleCommandSource().sendMessage(message);
            }
        });
    }

    @Override
    public Class<CommandExecuteEvent> eventClass() {
        return CommandExecuteEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.LAST;
    }
}
