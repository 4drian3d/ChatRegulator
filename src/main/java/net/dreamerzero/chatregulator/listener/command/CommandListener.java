package net.dreamerzero.chatregulator.listener.command;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
import net.dreamerzero.chatregulator.modules.checks.Check;
import net.dreamerzero.chatregulator.modules.checks.CommandCheck;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
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
    private final CommandCheck cCheck;
    private final UnicodeCheck uCheck;
    private final TypeUtils tUtils;
    private final Yaml config;

    /**
     * CommandListener constructor
     */
    public CommandListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist, Yaml messages) {
        this.server = server;
        this.cManager = new ConfigManager(messages, config);
        this.cUtils = new CommandUtils(server, config);
        this.dUtils = new DebugUtils(logger, config);
        this.fUtils = new FloodCheck(config);
        this.iUtils = new InfractionCheck(blacklist);
        this.cCheck = new CommandCheck(blacklist);
        this.tUtils = new TypeUtils(config);
        this.uCheck = new UnicodeCheck();
        this.config = config;
    }

    /**
     * Listener for command detections
     * @param event the command event
     */
    @Subscribe(async = true)
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

        cCheck.check(command);
        if(config.getBoolean("blocked-commands.enabled") &&
            !player.hasPermission("chatregulator.bypass.blocked-command")
            &&cCheck.isInfraction()){

                if(!callCommandViolationEvent(infractionPlayer, command, InfractionType.BCOMMAND, cCheck)) {
                    event.setResult(CommandResult.denied());
                    return;
                }
            }

        uCheck.check(command);
        if(config.getBoolean("unicode-blocker.enabled") &&
            !player.hasPermission("chatregulator.bypass.unicode")
            && uCheck.isInfraction()){
                if(!callCommandViolationEvent(infractionPlayer, command, InfractionType.UNICODE, uCheck)) {
                    event.setResult(CommandResult.denied());
                    return;
                }
        }

        fUtils.check(command);
        if(config.getBoolean("flood.enabled") &&
            !player.hasPermission("chatregulator.bypass.flood") &&
            fUtils.isInfraction()) {

            if(!callCommandViolationEvent(infractionPlayer, command, InfractionType.FLOOD, fUtils)) {
                event.setResult(CommandResult.denied());
                return;
            }
        }

        iUtils.check(command);
        if(config.getBoolean("infractions.enabled") &&
            !player.hasPermission("chatregulator.bypass.infractions") &&
            iUtils.isInfraction()) {

            if(!callCommandViolationEvent(infractionPlayer, command, InfractionType.REGULAR, iUtils)) {
                event.setResult(CommandResult.denied());
                return;
            }
        }

        SpamCheck sUtils = new SpamCheck(infractionPlayer, SourceType.COMMAND);
        sUtils.check(command);
        if(config.getBoolean("flood.enabled") &&
            !player.hasPermission("chatregulator.bypass.spam") &&
            sUtils.isInfraction() &&
            (config.getBoolean("spam.cooldown.enabled") && infractionPlayer.getTimeSinceLastCommand() < config.getLong("spam.cooldown.limit") || !config.getBoolean("spam.cooldown.enabled"))) {

            if(!callCommandViolationEvent(infractionPlayer, command, InfractionType.SPAM, sUtils)) {
                event.setResult(CommandResult.denied());
                return;
            }
        }

        infractionPlayer.lastCommand(command);
    }

    /**
     * Call command violation event
     * and approves player command
     * @param player Player who executed the command
     * @param command The command
     * @param type InfractionType to check
     * @return message of {@link CommandExecuteEvent} is approved
     */
    private boolean callCommandViolationEvent(InfractionPlayer player, String command, InfractionType type, Check detection) {
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new CommandViolationEvent(player, type, detection, command)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(command);
            } else {
                approved.set(false);
                dUtils.debug(player, command, type);
                violationEvent.addViolationGlobal(type);
                cManager.sendWarningMessage(player, type);

                cManager.sendAlertMessage(Audience.audience(
                    server.getAllPlayers().stream()
                        .filter(op -> op.hasPermission("chatregulator.notifications"))
                        .collect(Collectors.toList())),
                    player, type);

                player.addViolation(type);
                cUtils.executeCommand(type, player);
            }
        });
        return approved.get();
    }
}
