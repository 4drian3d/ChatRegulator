package net.dreamerzero.chatregulator.listener.command;

import java.util.Map;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.CommandViolationEvent;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.modules.SpamUtils;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class CommandListener {
    private final ProxyServer server;
    private Logger logger;
    private Yaml config;
    private Yaml blacklist;
    private Map<UUID, InfractionPlayer> infractionPlayers;

    public CommandListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist, Map<UUID, InfractionPlayer> infractionPlayers) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.blacklist = blacklist;
        this.infractionPlayers = infractionPlayers;
    }

    @Subscribe
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getCommandSource();
        InfractionPlayer infractionPlayer = infractionPlayers.get(player.getUniqueId());
        String command = event.getCommand();
        ConfigManager cManager = new ConfigManager(config);
        CommandUtils cUtils = new CommandUtils(server, config);
        FloodUtils fUtils = new FloodUtils(config);
        InfractionUtils iUtils = new InfractionUtils(blacklist);
        DebugUtils dUtils = new DebugUtils(fUtils, iUtils, logger, config);
        SpamUtils panUtils = new SpamUtils();

        if(!new TypeUtils(config).isCommand(command)) return;

        if(!player.hasPermission("chatregulator.bypass.flood") && fUtils.isFlood(command)){
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.FLOOD, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastCommand(command);
                } else {
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.FLOOD);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.FLOOD);
                    infractionPlayer.addViolation(InfractionType.FLOOD);
                    cUtils.executeCommand(InfractionType.FLOOD, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.FLOOD);
                    return;
                }
            });
        }

        if (!player.hasPermission("chatregulator.bypass.infraction") && iUtils.isInfraction(command)) {
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.REGULAR, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied() && command != infractionPlayer.lastCommand()) {
                    infractionPlayer.lastCommand(command);
                } else if(violationEvent.getResult() == GenericResult.allowed()){
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.REGULAR);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
                    infractionPlayer.addViolation(InfractionType.REGULAR);
                    cUtils.executeCommand(InfractionType.REGULAR, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.REGULAR);
                    return;
                }
            });
        }

        if(!player.hasPermission("chatregulator.bypass.spam") && panUtils.commandSpamInfricted(infractionPlayer, command)) {
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.SPAM, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastCommand(command);
                    return;
                } else {
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.REGULAR);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), player, InfractionType.REGULAR);
                    infractionPlayer.addViolation(InfractionType.SPAM);
                    cUtils.executeCommand(InfractionType.SPAM, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.SPAM);
                    return;
                }
            });
        }
    }
}
