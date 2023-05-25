package io.github._4drian3d.chatregulator.plugin.listener.command;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.event.CommandInfractionEvent;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import io.github._4drian3d.chatregulator.plugin.lazy.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.lazy.LazyDetection;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

public final class CommandListener implements RegulatorExecutor<CommandExecuteEvent> {
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
    @Inject
    @Named("command")
    private CheckProvider<CooldownCheck> cooldownProvider;
    @Inject
    private Logger logger;
    @Inject
    private EventManager eventManager;

    private boolean checkIfCanCheck(final String command) {
        for (final String cmd : configurationContainer.get().getCommandsChecked()) {
            if (Commands.isStartingString(command, cmd))
                return true;
        }
        return false;
    }

    @Override
    public @Nullable EventTask executeAsync(CommandExecuteEvent event) {
        if (!(event.getCommandSource() instanceof final Player player)
                || !event.getResult().isAllowed()
        ) {
            return null;
        }

        final InfractionPlayerImpl infractionPlayer = playerManager.getPlayer(player);

        return EventTask.resumeWhenComplete(
            LazyDetection.checks(
                    commandProvider,
                    syntaxProvider,
                    cooldownProvider
            ).detect(infractionPlayer, event.getCommand())
            .exceptionally(ex -> {
                logger.error("An error occurred while checking initial command checks", ex);
                return CheckResult.allowed();
            }).thenCompose(checkResult -> {
                if (checkResult.isDenied()) {
                    return CompletableFuture.completedFuture(checkResult);
                }

                if (!checkIfCanCheck(event.getCommand())) {
                    return CompletableFuture.completedFuture(CheckResult.allowed());
                }

                return LazyDetection.checks(
                        unicodeProvider,
                        capsProvider,
                        floodProvider,
                        infractionProvider,
                        spamProvider
                )
                .detect(infractionPlayer, event.getCommand())
                .exceptionally(ex -> {
                    logger.error("An error occurred while checking commands", ex);
                    return CheckResult.allowed();
                });
            }).handle((result, ex) -> {
                if (ex != null) {
                    logger.error("An error occurred while calculating command result", ex);
                } else {
                    if (result instanceof final CheckResult.DeniedCheckresult deniedResult) {
                        eventManager.fireAndForget(new CommandInfractionEvent(infractionPlayer, deniedResult.infractionType(), result, event.getCommand()));
                        infractionPlayer.onDenied(deniedResult, event.getCommand());
                        event.setResult(CommandExecuteEvent.CommandResult.denied());
                        return null;
                    }
                    if (result instanceof final CheckResult.ReplaceCheckResult replaceResult) {
                        final String replacedCommand = replaceResult.replaced();
                        infractionPlayer.getChain(SourceType.COMMAND).executed(replacedCommand);
                        event.setResult(CommandExecuteEvent.CommandResult.command(replacedCommand));
                    } else {
                        infractionPlayer.getChain(SourceType.COMMAND).executed(event.getCommand());
                        event.setResult(CommandExecuteEvent.CommandResult.allowed());
                    }
                }
                return null;
            }).exceptionally(ex -> {
                logger.error("An error occurred while setting chat result", ex);
                return null;
            })
        );
    }

    @Override
    public Class<CommandExecuteEvent> eventClass() {
        return CommandExecuteEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.EARLY;
    }
}
