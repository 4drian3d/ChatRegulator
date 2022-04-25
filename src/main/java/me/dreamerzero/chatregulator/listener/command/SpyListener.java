package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.modules.CommandSpy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Internal
public final class SpyListener {
    private final ChatRegulator plugin;

    public SpyListener(ChatRegulator plugin){
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.LAST)
    public EventTask onCommand(final CommandExecuteEvent event){
        final CommandSource source = event.getCommandSource();
        final MainConfig.CommandSpy config = Configuration.getConfig().getCommandSpyConfig();
        if(!event.getResult().isAllowed()
            || !(source instanceof final Player player)
            || !config.enabled()
            || source.hasPermission(Permissions.BYPASS_COMMANDSPY)
        ) {
            return null;
        }

        return EventTask.async(() -> {
            final String command = event.getCommand();

            if(CommandSpy.shouldAnnounce(source, command, config)){
                final Component message = plugin.getFormatter().parse(
                    Configuration.getMessages().getCommandSpyMessages().getMessage(),
                    player,
                    TagResolver.resolver(
                        Placeholder.unparsed("command", command),
                        Placeholder.unparsed("player", player.getUsername())
                    )
                );
                plugin.getProxy().getAllPlayers().forEach(pl -> {
                    if(pl.hasPermission(Permissions.COMMANDSPY_ALERT)) {
                        pl.sendMessage(message);
                    }
                });
                plugin.getProxy().getConsoleCommandSource().sendMessage(message);
            }
        });
    }
}
