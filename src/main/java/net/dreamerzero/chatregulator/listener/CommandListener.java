package net.dreamerzero.chatregulator.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.CommandViolationEvent;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class CommandListener {
    private final ProxyServer server;

    public CommandListener(final ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getCommandSource();
        InfractionPlayer infractionPlayer = Regulator.getInfractionPlayer(player.getUniqueId());
        String command = event.getCommand();

        if(!TypeUtils.isCommand(command)) return;

        if(FloodUtils.isFlood(command)){
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.FLOOD, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(CommandResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.FLOOD);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
            CommandUtils.executeCommand(InfractionType.FLOOD, player);
            DebugUtils.debug(infractionPlayer, command, InfractionType.FLOOD);
            return;
        }

        if (InfractionUtils.isInfraction(command)) {
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.REGULAR, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) return;
            });
            event.setResult(CommandResult.denied());
            ConfigManager.sendWarningMessage(player, InfractionType.REGULAR);
            ConfigManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
            CommandUtils.executeCommand(InfractionType.REGULAR, player);
            DebugUtils.debug(infractionPlayer, command, InfractionType.REGULAR);
            return;
        }
    }
}
