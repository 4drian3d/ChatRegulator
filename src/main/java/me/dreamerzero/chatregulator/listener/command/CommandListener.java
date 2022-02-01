package me.dreamerzero.chatregulator.listener.command;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.CommandCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.objects.AtomicString;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

/**
 * Detections related to command execution by players
 */
@Internal
public class CommandListener {
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
        final var messages = Configuration.getMessages();

        final AtomicString command = new AtomicString(event.getCommand());
        final Player player = (Player)event.getCommandSource();
        final InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, config.getCommandBlacklistConfig(), InfractionType.BCOMMAND)
            && CommandCheck.createCheck(command.get()).thenApply(result ->
                GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.BCOMMAND, result, SourceType.COMMAND, config.getCommandBlacklistConfig(), messages.getBlacklistMessages()))
                .join().booleanValue()){
                event.setResult(CommandResult.denied());
                    continuation.resume();
                return;
            }

        if(GeneralUtils.allowedPlayer(player, config.getUnicodeConfig(), InfractionType.UNICODE)
            && UnicodeCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.UNICODE, result, SourceType.COMMAND, config.getUnicodeConfig(), messages.getUnicodeMessages())){
                    if(config.getUnicodeConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return true;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
                return false;
            }).join().booleanValue()){
                return;
            }

        if(GeneralUtils.allowedPlayer(player, config.getCapsConfig(), InfractionType.CAPS)
            && CapsCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(),InfractionType.CAPS, result, SourceType.COMMAND, config.getCapsConfig(), messages.getCapsMessages())){
                    if(config.getCapsConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return true;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
                return false;
            }).join().booleanValue()){
                return;
            }


        if(GeneralUtils.allowedPlayer(player, config.getFloodConfig(), InfractionType.FLOOD)
            && FloodCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.FLOOD, result, SourceType.COMMAND, config.getFloodConfig(), messages.getFloodMessages())) {
                    if(config.getFloodConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return true;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
                return false;
            }).join().booleanValue()){
                return;
            }


        if(GeneralUtils.allowedPlayer(player, config.getInfractionsConfig(), InfractionType.REGULAR)
            && InfractionCheck.createCheck(command.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.REGULAR, result, SourceType.COMMAND, config.getInfractionsConfig(), messages.getInfractionsMessages())) {
                    if(config.getInfractionsConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return true;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
                return false;
            }).join().booleanValue()){
                return;
            }

        if(GeneralUtils.allowedPlayer(player, config.getSpamConfig(), InfractionType.SPAM)
            && SpamCheck.createCheck(infractionPlayer, command.get(), SourceType.COMMAND).thenApply(result ->
                GeneralUtils.spamCheck(result, config, infractionPlayer)
                && GeneralUtils.callViolationEvent(infractionPlayer, command.get(), InfractionType.SPAM, result, SourceType.COMMAND, config.getSpamConfig(), messages.getSpamMessages())
            ).join().booleanValue()){
                event.setResult(CommandResult.denied());
                continuation.resume();
                return;
            }

        infractionPlayer.lastCommand(command.get());
        continuation.resume();
    }
}
