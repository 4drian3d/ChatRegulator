package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.CommandCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.utils.CommandUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

/**
 * Detections related to command execution by players
 */
public class CommandListener {
    private final MainConfig.Config config;

    /**
     * CommandListener constructor
     */
    public CommandListener() {
        this.config = Configuration.getConfig();
    }

    /**
     * Listener for command detections
     * @param event the command event
     */
    @Subscribe
    public void onCommand(CommandExecuteEvent event, Continuation continuation){
        if (!(event.getCommandSource() instanceof Player)){
            continuation.resume();
            return;
        }

        String rawCommand = event.getCommand();

        String[] commandSplit = rawCommand.split(" ");
        if(!CommandUtils.isCommand(commandSplit[0])){
            continuation.resume();
            return;
        }        

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
            if(cCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, command, cCheck, SourceType.COMMAND)){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(command);
            if(uCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, command, uCheck, SourceType.COMMAND)){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(command);

            if(cCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, command, cCheck, SourceType.COMMAND)){
                if(config.getCapsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
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
            if(fCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, command, fCheck, SourceType.COMMAND)) {
                if(config.getFloodConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
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
            if(iCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, command, iCheck, SourceType.COMMAND)) {
                if(config.getInfractionsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                } else {
                    String commandReplaced = iCheck.replaceInfractions();
                    event.setResult(CommandResult.command(commandReplaced));
                    command = commandReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            SpamCheck sCheck = new SpamCheck(infractionPlayer, SourceType.COMMAND);
            sCheck.check(command);
            if(GeneralUtils.spamCheck(sCheck, config, infractionPlayer) && !GeneralUtils.callViolationEvent(infractionPlayer, command, sCheck, SourceType.COMMAND)) {
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        infractionPlayer.lastCommand(command);
        continuation.resume();
    }
}
