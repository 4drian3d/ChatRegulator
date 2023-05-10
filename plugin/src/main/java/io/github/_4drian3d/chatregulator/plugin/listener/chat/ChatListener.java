package io.github._4drian3d.chatregulator.plugin.listener.chat;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.event.ChatInfractionEvent;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.plugin.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.Replacer;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.lazy.LazyDetectionProvider;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;


public final class ChatListener implements RegulatorExecutor<PlayerChatEvent> {
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private PlayerManagerImpl playerManager;
    @Inject
    private CheckProvider<UnicodeCheck> unicodeProvider;
    @Inject
    private CheckProvider<CapsCheck> capsProvider;
    @Inject
    private CheckProvider<FloodCheck> floodProvider;
    @Inject
    private CheckProvider<RegexCheck> infractionProvider;
    @Inject
    @Named("chat")
    private CheckProvider<SpamCheck> spamProvider;
    @Inject
    private Logger logger;
    @Inject
    private EventManager eventManager;

    @Override
    public @Nullable EventTask executeAsync(PlayerChatEvent event) {
        if (!event.getResult().isAllowed()) {
            return null;
        }

        return EventTask.withContinuation(continuation -> {
            final InfractionPlayerImpl player = playerManager.getPlayer(event.getPlayer().getUniqueId());
            LazyDetectionProvider.checks(
                    unicodeProvider,
                    capsProvider,
                    floodProvider,
                    infractionProvider,
                    spamProvider
            )
            .detect(player, event.getMessage())
            .exceptionally(ex -> {
                logger.error("An error occurred while checking chat", ex);
                return CheckResult.allowed();
            })
            .thenAccept(checkResult -> {
                if (checkResult.isDenied()) {
                    final CheckResult.DeniedCheckresult deniedResult = (CheckResult.DeniedCheckresult) checkResult;
                    eventManager.fireAndForget(new ChatInfractionEvent(player, deniedResult.infractionType(), checkResult, event.getMessage()));
                    player.onDenied(deniedResult, event.getMessage());
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                } else {
                    String finalMessage = event.getMessage();
                    if (checkResult.shouldModify()) {
                        CheckResult.ReplaceCheckResult replaceResult = (CheckResult.ReplaceCheckResult) checkResult;
                        finalMessage = replaceResult.replaced();
                    }

                    final Configuration configuration = configurationContainer.get();
                    if (configuration.getFormatConfig().enabled()) {
                        finalMessage = Replacer.applyFormat(finalMessage, configuration);
                        event.setResult(ChatResult.message(finalMessage));
                    }
                    player.getChain(SourceType.CHAT).executed(finalMessage);
                    continuation.resume();
                }
            })
            .exceptionally(ex -> {
                logger.error("An error occurred while setting chat result", ex);
                return null;
            });
        });
    }

    @Override
    public Class<PlayerChatEvent> eventClass() {
        return PlayerChatEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.FIRST;
    }
}
