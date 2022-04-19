package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.CommandCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.SyntaxCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.objects.AtomicString;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.utils.CommandUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

/**
 * Detections related to command execution by players
 */
@Internal
public class CommandListener {
    private final ChatRegulator plugin;
    public CommandListener(ChatRegulator plugin){
        this.plugin = plugin;
    }
    /**
     * Listener for command detections
     * @param event the command event
     * @param continuation the event cycle
     */
    @Subscribe(order = PostOrder.FIRST)
    public void onCommand(CommandExecuteEvent event, Continuation continuation){
        if (!(event.getCommandSource() instanceof Player) || !event.getResult().isAllowed()){
            continuation.resume();
            return;
        }
        final MainConfig.Config config = Configuration.getConfig();
        if(this.checkIfCanCheck(event.getCommand(), config)){
            continuation.resume();
            return;
        }

        final AtomicString command = new AtomicString(event.getCommand());
        final Player player = (Player)event.getCommandSource();
        final InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.BCOMMAND)
            && CommandCheck.createCheck(command.get()).thenApply(result ->
                GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.BCOMMAND, result, SourceType.COMMAND, plugin))
                .join().booleanValue()){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SYNTAX)
            && SyntaxCheck.createCheck(command.get()).thenApply(result ->
                GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.SYNTAX, result, SourceType.COMMAND, plugin)
            ).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)
            && UnicodeCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.UNICODE, result, SourceType.COMMAND, plugin)){
                    if(config.getUnicodeConfig().isBlockable()){
                        return true;
                    }
                    if(result instanceof IReplaceable){
                        String commandReplaced = ((IReplaceable)result).replaceInfraction();
                        event.setResult(CommandResult.command(commandReplaced));
                        command.set(commandReplaced);
                    }
                }
                return false;
            }).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)
            && CapsCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(),InfractionType.CAPS, result, SourceType.COMMAND, plugin)){
                    if(config.getCapsConfig().isBlockable()){
                        return true;
                    }
                    if(result instanceof IReplaceable) {
                        String commandReplaced = ((IReplaceable)result).replaceInfraction();
                        event.setResult(CommandResult.command(commandReplaced));
                        command.set(commandReplaced);
                    }
                }
                return false;
            }).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }


        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)
            && FloodCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.FLOOD, result, SourceType.COMMAND, plugin)) {
                    if(config.getFloodConfig().isBlockable()){
                        return true;
                    }
                    if(result instanceof IReplaceable){
                        String commandReplaced = ((IReplaceable)result).replaceInfraction();
                        event.setResult(CommandResult.command(commandReplaced));
                        command.set(commandReplaced);
                    }
                }
                return false;
            }).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }


        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)
            && InfractionCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.REGULAR, result, SourceType.COMMAND, plugin)) {
                    if(config.getInfractionsConfig().isBlockable()){
                        return true;
                    }
                    if(result instanceof IReplaceable){
                        String commandReplaced = ((IReplaceable)result).replaceInfraction();
                        event.setResult(CommandResult.command(commandReplaced));
                        command.set(commandReplaced);
                    }
                }
                return false;
            }).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)
            && SpamCheck.createCheck(infractionPlayer, command.get(), SourceType.COMMAND).thenApply(result ->
                GeneralUtils.spamCheck(result, config, infractionPlayer)
                && GeneralUtils.callViolationEvent(infractionPlayer, command.get(), InfractionType.SPAM, result, SourceType.COMMAND, plugin)
            ).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }

        infractionPlayer.lastCommand(command.get());
        continuation.resume();
    }

    private boolean checkIfCanCheck(final String command, final MainConfig.Config config){
        for(final String cmd : config.getCommandsChecked()){
            if(CommandUtils.isStartingString(command, cmd))
                return true;
        }
        return false;
    }
}
