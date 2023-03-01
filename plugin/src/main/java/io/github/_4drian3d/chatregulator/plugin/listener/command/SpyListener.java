package io.github._4drian3d.chatregulator.plugin.listener.command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.modules.CommandSpy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Internal
public final class SpyListener {
    @Inject
    private ChatRegulator plugin;

    @Subscribe(order = PostOrder.LAST)
    public EventTask onCommand(final CommandExecuteEvent event){
        final CommandSource source = event.getCommandSource();
        final Configuration.CommandSpy config = plugin.getConfig().getCommandSpyConfig();
        if(!event.getResult().isAllowed()
            || !config.enabled()
            || !(source instanceof final Player player)
            || Permission.BYPASS_COMMAND_SPY.test(source)
        ) {
            return null;
        }

        return EventTask.async(() -> {
            final String command = event.getCommand();

            if(CommandSpy.shouldAnnounce(source, command, config)){
                final Component message = plugin.getFormatter().parse(
                    plugin.getMessages().getCommandSpyMessages().getMessage(),
                    player,
                    TagResolver.resolver(
                        Placeholder.unparsed("command", command),
                        Placeholder.unparsed("player", player.getUsername())
                    )
                );
                plugin.getProxy().getAllPlayers().forEach(pl -> {
                    if(Permission.COMMANDSPY_ALERT.test(pl)) {
                        pl.sendMessage(message);
                    }
                });
                plugin.getProxy().getConsoleCommandSource().sendMessage(message);
            }
        });
    }
}
