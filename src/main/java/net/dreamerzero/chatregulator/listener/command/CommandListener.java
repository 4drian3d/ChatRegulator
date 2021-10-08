package net.dreamerzero.chatregulator.listener.command;

import java.util.concurrent.atomic.AtomicBoolean;

import com.velocitypowered.api.event.PostOrder;
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
import net.dreamerzero.chatregulator.modules.Replacer;
import net.dreamerzero.chatregulator.modules.checks.Check;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;
import net.kyori.adventure.audience.Audience;

/**
 * Detections related to command execution by players
 */
public class CommandListener {
    private final ProxyServer server;
    private final ConfigManager cManager;
    private final CommandUtils cUtils;
    private final DebugUtils dUtils;
    private final FloodCheck fUtils;
    private final InfractionCheck iUtils;
    private final TypeUtils tUtils;
    private final Yaml config;
    private final Replacer rUtils;

    /**
     * CommandListener constructor
     */
    public CommandListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist) {
        this.server = server;
        this.cManager = new ConfigManager(config);
        this.cUtils = new CommandUtils(server, config);
        this.dUtils = new DebugUtils(logger, config);
        this.fUtils = new FloodCheck(config);
        this.iUtils = new InfractionCheck(blacklist);
        this.tUtils = new TypeUtils(config);
        this.rUtils = new Replacer(config);
        this.config = config;
    }

    /**
     * Listener for command detections
     * @param event the command event
     */
    @Subscribe(async = true, order = PostOrder.FIRST)
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player)) {
            return;
        }

        String rawCommand = event.getCommand();

        String commandSplit[] = rawCommand.split(" ");
        String realCommand = commandSplit[0];
        if(!tUtils.isCommand(realCommand)) return;

        StringBuilder sBuilder = new StringBuilder();

        for(int i = 1; i < commandSplit.length; i++){
            sBuilder.append(commandSplit[i]).append(" ");
        }

        String command = sBuilder.toString();

        Player player = (Player)event.getCommandSource();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        fUtils.check(command);
        if(config.getBoolean("flood.enabled") &&
            !player.hasPermission("chatregulator.bypass.flood") &&
            fUtils.isInfraction()) {

            if(!callCommandViolationEvent(infractionPlayer, event, InfractionType.FLOOD, fUtils)) {
                return;
            }
        }

        iUtils.check(command);
        if(config.getBoolean("infractions.enabled") &&
            !player.hasPermission("chatregulator.bypass.infractions") &&
            iUtils.isInfraction()) {

            if(!callCommandViolationEvent(infractionPlayer, event, InfractionType.REGULAR, iUtils)) {
                return;
            }
        }

        SpamCheck sUtils = new SpamCheck(infractionPlayer, SourceType.COMMAND);
        sUtils.check(command);
        if(config.getBoolean("flood.enabled") &&
            !player.hasPermission("chatregulator.bypass.spam") &&
            sUtils.isInfraction()) {

            if(!callCommandViolationEvent(infractionPlayer, event, InfractionType.SPAM, sUtils)) {
                return;
            }
        }

        if(config.getBoolean("format.enabled")){
            String formatted = rUtils.applyFormat(command);
            infractionPlayer.lastCommand(formatted);
            event.setResult(CommandResult.command(formatted));
        }

        infractionPlayer.lastCommand(command);
    }

    /**
     * Call command violation event
     * and approves player command
     * @param player Player who executed the command
     * @param event Event listening
     * @param type InfractionType to check
     * @return message of {@link CommandExecuteEvent} is approved
     */
    private boolean callCommandViolationEvent(InfractionPlayer player, CommandExecuteEvent event, InfractionType type, Check detection) {
        String command = event.getCommand();
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new CommandViolationEvent(player, type, detection, command)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(command);
            } else {
                dUtils.debug(player, command, type);
                violationEvent.addViolationGlobal(type);
                cManager.sendWarningMessage(player, type);
                cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                  op -> op.hasPermission("chatregulator.notifications")).toList()), player, type);
                event.setResult(CommandResult.denied());
                player.addViolation(type);
                cUtils.executeCommand(type, player);
                approved.set(false);
            }
        });
        return approved.get();
    }
}
