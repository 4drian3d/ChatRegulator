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
import io.github._4drian3d.chatregulator.api.lazy.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.api.lazy.LazyDetection;
import io.github._4drian3d.chatregulator.api.utils.Replacer;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.listener.RegulatorExecutor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
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
    @Named("chat")
    private CheckProvider<CooldownCheck> cooldownProvider;
    @Inject
    private Logger logger;
    @Inject
    private EventManager eventManager;

    @Override
    public @Nullable EventTask executeAsync(final PlayerChatEvent event) {
        if (!event.getResult().isAllowed()) {
            return null;
        }

        final InfractionPlayerImpl player = playerManager.getPlayer(event.getPlayer());

        return EventTask.resumeWhenComplete(
                LazyDetection.checks(
                    cooldownProvider,
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
                }).thenAccept(checkResult -> {
                    if (checkResult instanceof final CheckResult.DeniedCheckResult deniedResult) {
                        this.eventManager.fireAndForget(new ChatInfractionEvent(player, deniedResult.infractionType(), checkResult, event.getMessage()));
                        player.onDetection(deniedResult, event.getMessage());
                        event.setResult(ChatResult.denied());
                    } else {
                        String finalMessage;
                        if (checkResult instanceof final CheckResult.ReplaceCheckResult replaceResult) {
                            finalMessage = replaceResult.replaced();

                            this.eventManager.fireAndForget(new ChatInfractionEvent(player, replaceResult.infractionType(), checkResult, event.getMessage()));
                            player.onDetection(replaceResult, event.getMessage());
                        } else {
                            finalMessage = event.getMessage();
                        }

                        final Configuration.Formatter configuration = configurationContainer.get().getFormatterConfig();
                        if (configuration.enabled()) {
                            finalMessage = applyFormat(finalMessage, configuration);
                        }

                        if (finalMessage.isEmpty()) {
                            event.setResult(ChatResult.denied());
                        } else {
                            player.getChain(SourceType.CHAT).executed(event.getMessage());
                            if (!finalMessage.equals(event.getMessage())) {
                                event.setResult(ChatResult.message(finalMessage));
                            }
                        }
                    }
                }).exceptionally(ex -> {
                    logger.error("An error occurred while setting chat result", ex);
                    return null;
                })
        );
    }

    @Override
    public Class<PlayerChatEvent> eventClass() {
        return PlayerChatEvent.class;
    }

    @Override
    public PostOrder postOrder() {
        return PostOrder.EARLY;
    }

    public static @NotNull String applyFormat(final @NotNull String string, Configuration.Formatter config) {
        return firstLetterUppercase(addFinalDot(unicodeNormalize(string, config), config), config);
    }
    public static @NotNull String firstLetterUppercase(@NotNull final String string, Configuration.Formatter config) {
        if (!config.setFirstLetterUppercase()) return string;
        return Replacer.firstLetterUppercase(string);
    }
    public static String addFinalDot(final String string, Configuration.Formatter config) {
        return config.setFinalDot()
                ? Replacer.addFinalDot(string)
                : string;
    }

    public static @NotNull String unicodeNormalize(@NotNull final String string, Configuration.Formatter config) {
        return config.setUnicodeNormalize() ? Replacer.unicodeNormalize(string, config.setUnicodeNormalizationForm()) : string;
    }
}
