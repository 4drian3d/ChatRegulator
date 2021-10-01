package net.dreamerzero.chatregulator.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.FloodUtils;
import net.dreamerzero.chatregulator.utils.InfractionUtils;
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
        String message = event.getMessage();
        Player player = event.getPlayer();
        TypeUtils.InfractionType detection = InfractionType.NONE;
        boolean detected = false;

        if(FloodUtils.isFlood(message)) {
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.FLOOD);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("regulator.notifications")).toList()), player, InfractionType.FLOOD);
            CommandUtils.executeCommand(InfractionType.FLOOD, player);
            detection = InfractionType.FLOOD;
            detected = true;
        }

        if(InfractionUtils.isInfraction(message)) {
            event.setResult(ChatResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.REGULAR);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("regulator.notifications")).toList()), player, InfractionType.REGULAR);
            CommandUtils.executeCommand(InfractionType.REGULAR, player);
            detection = InfractionType.REGULAR;
            detected = true;
        }

        if (detected && Regulator.getConfig().getBoolean("debug")){
            logger.info("User Detected: {}", player.getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("Message: {}", message);
            logger.info("Pattern: {}", detection == InfractionType.FLOOD ? FloodUtils.getFloodPattern() : InfractionUtils.getPattern(message));
        }
    }
}
