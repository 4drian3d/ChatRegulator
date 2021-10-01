package net.dreamerzero.chatregulator.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.Regulator;
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

    public ChatListener(final ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        InfractionPlayer infractionPlayer = Regulator.getInfractionPlayer(player.getUniqueId());

        if(!player.hasPermission("chatregulator.bypass.flood") && FloodUtils.isFlood(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.FLOOD, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.FLOOD);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
            CommandUtils.executeCommand(InfractionType.FLOOD, player);
            infractionPlayer.addViolation(InfractionType.FLOOD);
            DebugUtils.debug(infractionPlayer, message, InfractionType.FLOOD);
            if(Regulator.getConfig().getInt("flood.commands.violations-required") == infractionPlayer.getFloodInfractions()){
                CommandUtils.executeCommand(InfractionType.REGULAR, player);
            }
            return;
        }

        if(!player.hasPermission("chatregulator.bypass.infractions") && InfractionUtils.isInfraction(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.REGULAR, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.REGULAR);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
            infractionPlayer.addViolation(InfractionType.REGULAR);
            DebugUtils.debug(infractionPlayer, message, InfractionType.REGULAR);
            if(Regulator.getConfig().getInt("infractions.commands.violations-required") == infractionPlayer.getRegularInfractions()){
                CommandUtils.executeCommand(InfractionType.REGULAR, player);
            }
            return;
        }

        if(!player.hasPermission("chatregulator.bypass.spam") && infractionPlayer.getLastMessage().equalsIgnoreCase(message)) {
            server.getEventManager().fire(new ChatViolationEvent(infractionPlayer, InfractionType.SPAM, message)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(ChatResult.denied());
            infractionPlayer.addViolation(InfractionType.SPAM);
            DebugUtils.debug(infractionPlayer, message, InfractionType.SPAM);
            return;
        }

        infractionPlayer.setLastMessage(message);
    }
}
