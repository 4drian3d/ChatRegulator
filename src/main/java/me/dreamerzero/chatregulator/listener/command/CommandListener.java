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
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.utils.AtomicString;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

/**
 * Detections related to command execution by players
 */
@Internal
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

        final AtomicString command = new AtomicString(event.getCommand());
        final Player player = (Player)event.getCommandSource();
        final InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.BCOMMAND)){
            new CommandCheck().check(command.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.BCOMMAND, result, SourceType.COMMAND)){
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            new UnicodeCheck().check(command.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.UNICODE, result, SourceType.COMMAND)){
                    if(config.getUnicodeConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            new CapsCheck().check(command.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(),InfractionType.CAPS, result, SourceType.COMMAND)){
                    if(config.getCapsConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            new FloodCheck().check(command.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.FLOOD, result, SourceType.COMMAND)) {
                    if(config.getFloodConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            new InfractionCheck().check(command.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractionPlayer, command.get(), InfractionType.REGULAR, result, SourceType.COMMAND)) {
                    if(config.getInfractionsConfig().isBlockable()){
                        event.setResult(CommandResult.denied());
                        continuation.resume();
                        return;
                    }
                    String commandReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(CommandResult.command(commandReplaced));
                    command.set(commandReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            new SpamCheck(infractionPlayer, SourceType.COMMAND).check(command.get()).thenAccept(result -> {
                if(GeneralUtils.spamCheck(result, config, infractionPlayer) && GeneralUtils.callViolationEvent(infractionPlayer, command.get(), InfractionType.SPAM, result, SourceType.COMMAND)) {
                    event.setResult(CommandResult.denied());
                    continuation.resume();
                    return;
                }
            }).join();
        }

        infractionPlayer.lastCommand(command.get());
        continuation.resume();
    }
}
