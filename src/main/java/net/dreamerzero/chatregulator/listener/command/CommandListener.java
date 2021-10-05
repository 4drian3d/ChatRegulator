package net.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.CommandViolationEvent;
import net.dreamerzero.chatregulator.modules.FloodCheck;
import net.dreamerzero.chatregulator.modules.InfractionCheck;
import net.dreamerzero.chatregulator.modules.SpamCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.audience.Audience;

public class CommandListener {
    private final ProxyServer server;
    private Logger logger;
    private Yaml config;
    private Yaml blacklist;

    public CommandListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist) {
        this.server = server;
        this.logger = logger;
        this.config = config;
        this.blacklist = blacklist;
    }

    @Subscribe(async = true)
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        Player player = (Player)event.getCommandSource();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
        String command = event.getCommand();
        ConfigManager cManager = new ConfigManager(config);
        CommandUtils cUtils = new CommandUtils(server, config);
        DebugUtils dUtils = new DebugUtils(logger, config);

        if(!new TypeUtils(config).isCommand(command)) return;

        FloodCheck fUtils = new FloodCheck(config);
        fUtils.check(command);
        if(!player.hasPermission("chatregulator.bypass.flood") && fUtils.isInfraction()){
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.FLOOD, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastCommand(command);
                } else {
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.FLOOD);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.FLOOD);
                    infractionPlayer.addViolation(InfractionType.FLOOD);
                    cUtils.executeCommand(InfractionType.FLOOD, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.FLOOD, fUtils);
                    return;
                }
            });
        }

        InfractionCheck iUtils = new InfractionCheck(blacklist);
        iUtils.check(command);
        if (!player.hasPermission("chatregulator.bypass.infraction") && iUtils.isInfraction()) {
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.REGULAR, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied() && command != infractionPlayer.lastCommand()) {
                    infractionPlayer.lastCommand(command);
                } else if(violationEvent.getResult() == GenericResult.allowed()){
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.REGULAR);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.REGULAR);
                    infractionPlayer.addViolation(InfractionType.REGULAR);
                    cUtils.executeCommand(InfractionType.REGULAR, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.REGULAR, iUtils);
                    return;
                }
            });
        }

        SpamCheck panUtils = new SpamCheck(logger, infractionPlayer);
        if(!player.hasPermission("chatregulator.bypass.spam") && panUtils.commandSpamInfricted(command)) {
            server.getEventManager().fire(new CommandViolationEvent(infractionPlayer, InfractionType.SPAM, command)).thenAccept(violationEvent -> {
                if(violationEvent.getResult() == GenericResult.denied()) {
                    infractionPlayer.lastCommand(command);
                    return;
                } else {
                    event.setResult(CommandResult.denied());
                    cManager.sendWarningMessage(player, InfractionType.REGULAR);
                    cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                        op -> op.hasPermission("chatregulator.notifications")).toList()), infractionPlayer, InfractionType.SPAM);
                    infractionPlayer.addViolation(InfractionType.SPAM);
                    cUtils.executeCommand(InfractionType.SPAM, infractionPlayer);
                    dUtils.debug(infractionPlayer, command, InfractionType.SPAM);
                    return;
                }
            });
        }
    }
}
