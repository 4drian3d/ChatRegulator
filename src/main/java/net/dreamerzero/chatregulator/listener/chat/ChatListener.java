package net.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.ChatViolationEvent;
import net.dreamerzero.chatregulator.modules.FloodCheck;
import net.dreamerzero.chatregulator.modules.InfractionCheck;
import net.dreamerzero.chatregulator.modules.SpamCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class ChatListener {
    private final ProxyServer server;
    private Logger logger;
    private Yaml config;
    private Yaml blacklist;

    /**
     * ChatListener Constructor
     * @param server the proxy server
     * @param logger the logger
     * @param config the plugin config
     * @param blacklist the blacklist config
     */
    public ChatListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.blacklist = blacklist;
    }

    @Subscribe(async = true)
    public void onChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
        ConfigManager cManager = new ConfigManager(config);
        CommandUtils cUtils = new CommandUtils(server, config);
        DebugUtils dUtils = new DebugUtils(logger, config);

        FloodCheck fUtils = new FloodCheck(config);
        fUtils.check(message);
        if(!player.hasPermission("chatregulator.bypass.flood") && fUtils.isInfraction()) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.FLOOD, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastMessage(message);
                } else {
                    dUtils.debug(infractionPlayer, message, InfractionType.FLOOD, fUtils);
                    event.setResult(ChatResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.FLOOD, fUtils);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.FLOOD);
                    cUtils.executeCommand(InfractionType.FLOOD, infractionPlayer);
                    infractionPlayer.addViolation(InfractionType.FLOOD);
                    cUtils.executeCommand(InfractionType.FLOOD, infractionPlayer);
                    return;
                }
            });
        }

        InfractionCheck iUtils = new InfractionCheck(blacklist);
        iUtils.check(message);
        if(!player.hasPermission("chatregulator.bypass.infractions") && iUtils.isInfraction()) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.REGULAR, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied() && message != infractionPlayer.lastMessage()) {
                    infractionPlayer.lastMessage(message);
                } else if(violationEvent.getResult() == GenericResult.allowed()) {
                    dUtils.debug(infractionPlayer, message, InfractionType.REGULAR, iUtils);
                    event.setResult(ChatResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.REGULAR, iUtils);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.REGULAR);
                    infractionPlayer.addViolation(InfractionType.REGULAR);
                    cUtils.executeCommand(InfractionType.REGULAR, infractionPlayer);
                    return;
                }
            });
        }

        SpamCheck panUtils = new SpamCheck(infractionPlayer);
        if(!player.hasPermission("chatregulator.bypass.spam") && panUtils.messageSpamInfricted(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.SPAM, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastMessage(message);
                    return;
                } else {
                    dUtils.debug(infractionPlayer, message, InfractionType.SPAM);
                    cManager.sendWarningMessage(player, InfractionType.SPAM);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.SPAM);
                    event.setResult(ChatResult.denied());
                    infractionPlayer.addViolation(InfractionType.SPAM);
                    cUtils.executeCommand(InfractionType.SPAM, infractionPlayer);

                    return;
                }
            });
        }

        infractionPlayer.lastMessage(message);
    }
}
