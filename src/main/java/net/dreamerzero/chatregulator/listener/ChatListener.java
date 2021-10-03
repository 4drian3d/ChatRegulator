package net.dreamerzero.chatregulator.listener;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.ChatViolationEvent;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class ChatListener {
    private final ProxyServer server;
    private Logger logger;
    private Yaml config;
    private Yaml blacklist;
    private Map<UUID, InfractionPlayer> infractionPlayers;

    public ChatListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist, Map<UUID, InfractionPlayer> infractionPlayers) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.blacklist = blacklist;
        this.infractionPlayers = infractionPlayers;
    }

    @Subscribe
    public void onChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        InfractionPlayer infractionPlayer = infractionPlayers.get(player.getUniqueId());
        ConfigManager cManager = new ConfigManager(config);
        CommandUtils cUtils = new CommandUtils(server, config);
        FloodUtils fUtils = new FloodUtils(config);
        InfractionUtils iUtils = new InfractionUtils(blacklist);
        DebugUtils dUtils = new DebugUtils(fUtils, iUtils, logger, config);
        

        if(!player.hasPermission("chatregulator.bypass.flood") && fUtils.isFlood(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.FLOOD, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            cManager.sendWarningMessage(player, InfractionType.FLOOD);
            cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
                cUtils.executeCommand(InfractionType.FLOOD, player);
            infractionPlayer.addViolation(InfractionType.FLOOD);
            dUtils.debug(infractionPlayer, message, InfractionType.FLOOD);
            if(config.getInt("flood.commands.violations-required") == infractionPlayer.getFloodInfractions()){
                cUtils.executeCommand(InfractionType.REGULAR, player);
            }
            return;
        }

        if(!player.hasPermission("chatregulator.bypass.infractions") && iUtils.isInfraction(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.REGULAR, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            cManager.sendWarningMessage(player, InfractionType.REGULAR);
            cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
            infractionPlayer.addViolation(InfractionType.REGULAR);
            dUtils.debug(infractionPlayer, message, InfractionType.REGULAR);
            if(config.getInt("infractions.commands.violations-required") == infractionPlayer.getRegularInfractions()){
                cUtils.executeCommand(InfractionType.REGULAR, player);
            }
            return;
        }

        if(!player.hasPermission("chatregulator.bypass.spam") && infractionPlayer.getLastMessage().equalsIgnoreCase(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.SPAM, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            infractionPlayer.addViolation(InfractionType.SPAM);
            dUtils.debug(infractionPlayer, message, InfractionType.SPAM);
            return;
        }

        infractionPlayer.setLastMessage(message);
    }
}
