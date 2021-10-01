package net.dreamerzero.chatregulator.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class CommandListener {
    private final ProxyServer server;
    private final Logger logger;

    public CommandListener(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getCommandSource();
        String command = event.getCommand();
        TypeUtils.InfractionType detection = InfractionType.NONE;
        boolean detected = false;

        if(!TypeUtils.isCommand(command)) return;

        if(FloodUtils.isFlood(command)){
            event.setResult(CommandResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.FLOOD);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
            CommandUtils.executeCommand(InfractionType.FLOOD, player);
            detection = InfractionType.FLOOD;
            detected = true;
        }

        if (InfractionUtils.isInfraction(command)) {
            event.setResult(CommandResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.REGULAR);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
            CommandUtils.executeCommand(InfractionType.REGULAR, player);
            detection = InfractionType.REGULAR;
            detected = true;
        }

        if (detected && Regulator.getConfig().getBoolean("debug")){
            logger.info("User Detected: {}", player.getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("Command: {}", command);
            logger.info("Pattern: {}", detection == InfractionType.REGULAR ? FloodUtils.getFloodPattern() : InfractionUtils.getPattern(command));
        }
    }
}
