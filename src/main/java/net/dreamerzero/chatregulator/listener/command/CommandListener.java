package net.dreamerzero.chatregulator.listener.command;

import java.util.concurrent.atomic.AtomicBoolean;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;
import net.dreamerzero.chatregulator.events.CommandViolationEvent;
import net.dreamerzero.chatregulator.modules.Statistics;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.modules.checks.CapsCheck;
import net.dreamerzero.chatregulator.modules.checks.CommandCheck;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.GeneralUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;

/**
 * Detections related to command execution by players
 */
public class CommandListener {
    private final ProxyServer server;
    private final ConfigManager cManager;
    private final CommandUtils cUtils;
    private final MainConfig.Config config;

    /**
     * CommandListener constructor
     * @param server the proxy server
     */
    public CommandListener(final ProxyServer server) {
        this.server = server;
        this.config = Configuration.getConfig();
        this.cManager = new ConfigManager();
        this.cUtils = new CommandUtils(server);
    }

    /**
     * Listener for command detections
     * @param event the command event
     */
    @Subscribe(async = true)
    public void onCommand(CommandExecuteEvent event){
        if (!(event.getCommandSource() instanceof Player))
            return;

        String rawCommand = event.getCommand();

        String[] commandSplit = rawCommand.split(" ");
        if(!CommandUtils.isCommand(commandSplit[0])) return;

        StringBuilder sBuilder = new StringBuilder();

        for(int i = 1; i < commandSplit.length; i++){
            sBuilder.append(commandSplit[i]).append(" ");
        }

        String command = sBuilder.toString();

        Player player = (Player)event.getCommandSource();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.BCOMMAND)){
            CommandCheck cCheck = new CommandCheck();
            cCheck.check(command);
            if(cCheck.isInfraction() && !callCommandViolationEvent(infractionPlayer, command, InfractionType.BCOMMAND, cCheck)){
                event.setResult(CommandResult.denied());
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(command);
            if(uCheck.isInfraction() && !callCommandViolationEvent(infractionPlayer, command, InfractionType.UNICODE, uCheck)){
                event.setResult(CommandResult.denied());
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(command);

            if(cCheck.isInfraction() && !callCommandViolationEvent(infractionPlayer, command, InfractionType.CAPS, cCheck)){
                if(config.getCapsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    return;
                } else {
                    String commandReplaced = cCheck.replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command = commandReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            FloodCheck fCheck = new FloodCheck();
            fCheck.check(command);
            if(fCheck.isInfraction() && !callCommandViolationEvent(infractionPlayer, command, InfractionType.FLOOD, fCheck)) {
                if(config.getFloodConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    return;
                } else {
                    String commandReplaced = fCheck.replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command = commandReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            InfractionCheck iCheck = new InfractionCheck();
            iCheck.check(command);
            if(iCheck.isInfraction() && !callCommandViolationEvent(infractionPlayer, command, InfractionType.REGULAR, iCheck)) {
                if(config.getInfractionsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    return;
                } else {
                    String commandReplaced = iCheck.replaceInfractions();
                    event.setResult(CommandResult.command(commandReplaced));
                    command = commandReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            SpamCheck sUtils = new SpamCheck(infractionPlayer, SourceType.COMMAND);
            sUtils.check(command);
            var cooldownconfig = config.getSpamConfig().getCooldownConfig();
            if(sUtils.isInfraction()
                && (cooldownconfig.enabled() && infractionPlayer.getTimeSinceLastCommand() < cooldownconfig.limit()
                || !cooldownconfig.enabled())
                && !callCommandViolationEvent(infractionPlayer, command, InfractionType.SPAM, sUtils)) {

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
    private boolean callCommandViolationEvent(InfractionPlayer player, String command, InfractionType type, AbstractCheck detection) {
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new CommandViolationEvent(player, type, detection, command)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(command);
            } else {
                approved.set(false);
                DebugUtils.debug(player, command, type, detection);
                Statistics.addViolationCount(type);
                cManager.sendWarningMessage(player, type, detection);
                cManager.sendAlertMessage(server, player, type);

                player.getViolations().addViolation(type);
                cUtils.executeCommand(type, player);
            }
        });
        return approved.get();
    }
}
