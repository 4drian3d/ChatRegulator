package net.dreamerzero.chatregulator.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class ChatListener {
    private final ProxyServer server;
    private final Logger logger;

    public ChatListener(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        TypeUtils.InfractionType detection = InfractionType.NONE;
        boolean detected = false;
        InfractionPlayer infractionPlayer = Regulator.getInfractionPlayers().get(player.getUniqueId());

        if(!player.hasPermission("chatregulator.bypass.flood") && FloodUtils.isFlood(message)) {
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.FLOOD);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
            CommandUtils.executeCommand(InfractionType.FLOOD, player);
            infractionPlayer.addViolation(InfractionType.FLOOD);
            detection = InfractionType.FLOOD;
            detected = true;
            if(Regulator.getConfig().getInt("flood.commands.violations-required") == infractionPlayer.getFloodInfractions()){
                CommandUtils.executeCommand(InfractionType.REGULAR, player);
            }
        }

        if(!player.hasPermission("chatregulator.bypass.infractions") && InfractionUtils.isInfraction(message)) {
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.REGULAR);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
            infractionPlayer.addViolation(InfractionType.REGULAR);
            detection = InfractionType.REGULAR;
            detected = true;
            if(Regulator.getConfig().getInt("infractions.commands.violations-required") == infractionPlayer.getRegularInfractions()){
                CommandUtils.executeCommand(InfractionType.REGULAR, player);
            }
        }

        if(!player.hasPermission("chatregulator.bypass.spam") && infractionPlayer.getLastMessage().equalsIgnoreCase(message)) {
            event.setResult(ChatResult.denied());
            infractionPlayer.addViolation(InfractionType.SPAM);
            detection = InfractionType.SPAM;
            detected = true;
        }

        infractionPlayer.setLastMessage(message);

        if (detected && Regulator.getConfig().getBoolean("debug")){
            logger.info("User Detected: {}", player.getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("Message: {}", message);
            logger.info("Pattern: {}", detection == InfractionType.FLOOD ? FloodUtils.getFloodPattern() : InfractionUtils.getPattern(message));
        }
    }
}
