package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
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
import me.dreamerzero.chatregulator.utils.GeneralUtils;
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
    @Subscribe(order = PostOrder.FIRST)
    public void onCommand(CommandExecuteEvent event, Continuation continuation){
        if (!(event.getCommandSource() instanceof Player) || !event.getResult().isAllowed()){
            continuation.resume();
            return;
        }

        String command = event.getCommand();
        final Player player = (Player)event.getCommandSource();
        final InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.BCOMMAND)){
            CommandCheck cCheck = new CommandCheck();
            cCheck.check(command);
            if(GeneralUtils.checkAndCall(infractionPlayer, command, cCheck, SourceType.COMMAND)){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(command);
            if(GeneralUtils.checkAndCall(infractionPlayer, command, uCheck, SourceType.COMMAND)){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(command);

            if(cCheck.isInfraction() && GeneralUtils.callViolationEvent(infractionPlayer, command, cCheck, SourceType.COMMAND)){
                if(config.getCapsConfig().isBlockable()){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }
                String commandReplaced = cCheck.replaceInfraction();
                event.setResult(CommandResult.command(commandReplaced));
                command = commandReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            FloodCheck fCheck = new FloodCheck();
            fCheck.check(command);
            if(fCheck.isInfraction() && GeneralUtils.checkAndCall(infractionPlayer, command, fCheck, SourceType.COMMAND)) {
                if(config.getFloodConfig().isBlockable()){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }
                String commandReplaced = fCheck.replaceInfraction();
                event.setResult(CommandResult.command(commandReplaced));
                command = commandReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            InfractionCheck iCheck = new InfractionCheck();
            iCheck.check(command);
            if(GeneralUtils.checkAndCall(infractionPlayer, command, iCheck, SourceType.COMMAND)) {
                if(config.getInfractionsConfig().isBlockable()){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }
                String commandReplaced = iCheck.replaceInfractions();
                event.setResult(CommandResult.command(commandReplaced));
                command = commandReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            SpamCheck sCheck = new SpamCheck(infractionPlayer, SourceType.COMMAND);
            sCheck.check(command);
            if(GeneralUtils.spamCheck(sCheck, config, infractionPlayer) && GeneralUtils.callViolationEvent(infractionPlayer, command, sCheck, SourceType.COMMAND)) {
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }
        }

        infractionPlayer.lastCommand(command);
        continuation.resume();
    }
}
