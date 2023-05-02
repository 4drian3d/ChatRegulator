package io.github._4drian3d.chatregulator.plugin.listener.command;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import io.github._4drian3d.chatregulator.plugin.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.lazy.LazyDetectionProvider;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.CompletableFuture;

public final class CommandListener implements AwaitingEventExecutor<CommandExecuteEvent> {
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private PlayerManagerImpl playerManager;
    @Inject
    private CheckProvider<CommandCheck> commandProvider;
    @Inject
    private CheckProvider<SyntaxCheck> syntaxProvider;
    @Inject
    private CheckProvider<UnicodeCheck> unicodeProvider;
    @Inject
    private CheckProvider<CapsCheck> capsProvider;
    @Inject
    private CheckProvider<FloodCheck> floodProvider;
    @Inject
    private CheckProvider<RegexCheck> infractionProvider;
    @Inject
    @Named("command")
    private CheckProvider<SpamCheck> spamProvider;

    private boolean checkIfCanCheck(final String command) {
        for (final String cmd : configurationContainer.get().getCommandsChecked()) {
            if (Commands.isStartingString(command, cmd))
                return true;
        }
        return false;
    }

    // PostOrder.FIRST
    @Override
    public @Nullable EventTask executeAsync(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof final Player player)
                || !event.getResult().isAllowed()
        ) {
            return null;
        }

        return EventTask.withContinuation(continuation -> {
            final InfractionPlayerImpl infractionPlayer = playerManager.getPlayer(player);

            LazyDetectionProvider.checks(
                    commandProvider,
                    syntaxProvider
            ).detect(infractionPlayer, event.getCommand())
            .thenCompose(checkResult -> {
                if (checkResult.isDenied()) {
                    return CompletableFuture.completedFuture(CommandExecuteEvent.CommandResult.denied());
                }

                if (!checkIfCanCheck(event.getCommand())) {
                    return CompletableFuture.completedFuture(CommandExecuteEvent.CommandResult.allowed());
                }

                return LazyDetectionProvider.checks(
                        unicodeProvider,
                        capsProvider,
                        floodProvider,
                        infractionProvider,
                        spamProvider
                )
                .detect(infractionPlayer, event.getCommand())
                .thenApply(checkResult1 -> {
                    if (checkResult1.isDenied()){
                        return CommandExecuteEvent.CommandResult.denied();
                    }
                    if (checkResult.shouldModify()) {
                        final CheckResult.ReplaceCheckResult replaceResult = (CheckResult.ReplaceCheckResult) checkResult;
                        final String replacedCommand = replaceResult.replaced();
                        infractionPlayer.getChain(SourceType.COMMAND).executed(replacedCommand);
                        return CommandExecuteEvent.CommandResult.command(replacedCommand);
                    }
                    infractionPlayer.getChain(SourceType.COMMAND).executed(event.getCommand());
                    return CommandExecuteEvent.CommandResult.allowed();
                });
            }).thenAccept(commandResult -> {
                event.setResult(commandResult);
                continuation.resume();
            });
        });
    }
}
