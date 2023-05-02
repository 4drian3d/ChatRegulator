package io.github._4drian3d.chatregulator.plugin.listener.chat;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.velocitypowered.api.event.*;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.plugin.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.Replacer;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.lazy.LazyDetectionProvider;
import org.checkerframework.checker.nullness.qual.Nullable;


public final class ChatListener implements AwaitingEventExecutor<PlayerChatEvent> {
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

    // PostOrder.FIRST
    @Override
    public @Nullable EventTask executeAsync(PlayerChatEvent event) {
        if (!event.getResult().isAllowed()) {
            return null;
        }

        return EventTask.withContinuation(continuation -> {
            final InfractionPlayerImpl infractor = playerManager.getPlayer(event.getPlayer().getUniqueId());

            LazyDetectionProvider.checks(
                    unicodeProvider,
                    capsProvider,
                    floodProvider,
                    infractionProvider,
                    spamProvider
            )
            .detect(infractor, event.getMessage())
            .thenAccept(checkResult -> {
                if (checkResult.isDenied()) {
                    event.setResult(ChatResult.denied());
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
                    infractor.getChain(SourceType.CHAT).executed(finalMessage);
                }
                continuation.resume();
            });
        });
    }
}
