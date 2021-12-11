package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.modules.CommandSpy;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

public class SpyListener {
    private MainConfig.CommandSpy config;
    private final MiniMessage mm;
    private Messages.CommandSpy messages;

    public SpyListener(){
        this.config = Configuration.getConfig().getCommandSpyConfig();
        this.mm = MiniMessage.miniMessage();
        this.messages = Configuration.getMessages().getCommandSpyMessages();
    }

    @Subscribe(order = PostOrder.LAST)
    public void onCommand(CommandExecuteEvent event, Continuation continuation){
        CommandSource source = event.getCommandSource();
        if(!event.getResult().isAllowed() || !(source instanceof Player) || !config.enabled()){
            continuation.resume();
            return;
        }
        String command = event.getCommand();

        if(CommandSpy.shouldAnnounce(source, command, config)){
            ChatRegulator.getInstance().getProxy().getAllPlayers().stream()
                .filter(p -> p.hasPermission("chatregulator.commandspy.alert"))
                .forEach(p -> p.sendMessage(
                    mm.deserialize(
                        messages.getMessage(),
                        PlaceholderResolver.resolving(
                            "command", command,
                            "player", ((Player)source).getUsername()
                        )
                    )
                ));
        }
        continuation.resume();
    }
}
