package io.github._4drian3d.chatregulator.plugin.impl;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.common.impl.InfractionPlayerBase;
import io.github._4drian3d.chatregulator.common.impl.StringChainImpl;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.Formatter;
import io.github._4drian3d.chatregulator.plugin.source.RegulatorCommandSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class InfractionPlayerImpl extends InfractionPlayerBase {
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final FileLogger fileLogger;
    private final StringChainImpl commandChain;
    private final StringChainImpl chatChain;

    public InfractionPlayerImpl(
            @NotNull UUID playerUUID,
            @NotNull Function<UUID, Audience> playerConverter,
            ProxyServer proxyServer,
            ConfigurationContainer<Checks> checksContainer,
            Logger logger,
            FileLogger fileLogger
    ) {
        super(playerUUID, playerConverter);
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.fileLogger = fileLogger;
        this.commandChain = new StringChainImpl(checksContainer);
        this.chatChain = new StringChainImpl(checksContainer);
    }

//    @VisibleForTesting
//    public InfractionPlayerImpl(
//            @NotNull UUID playerUUID,
//            @NotNull Function<UUID, Audience> playerConverter
//    ) {
//        this(playerUUID, null, playerConverter);
//    }

    @Override
    public @NotNull StringChainImpl getChain(final @NotNull SourceType sourceType) {
        return switch (sourceType) {
            case CHAT -> chatChain;
            case COMMAND -> commandChain;
        };
    }

    @Override
    protected void sendAlertMessage(
            final ConfigurationContainer<Checks> checksContainer,
            final ConfigurationContainer<Messages> messagesContainer,
            final ConfigurationContainer<Configuration> configurationContainer,
            final Formatter formatter,
            final CheckResult.DetectedResult result,
            final String original
    ) {
        final InfractionType type = result.infractionType();
        final Messages.Alert messages = requireNonNull(messagesContainer.get().getAlert(type));
        final String alertMessage = messages.getAlertMessage();
        if (alertMessage.isBlank()) {
            return;
        }

        final TagResolver.Builder builder = TagResolver.builder();
        builder.resolver(getPlaceholders());

        if (result instanceof final CheckResult.ReplaceCheckResult replaceResult) {
            builder.resolver(Placeholder.unparsed("string", replaceResult.replaced()));
        } else {
            builder.resolver(Placeholder.unparsed("string", original));
        }
        builder.resolver(Placeholder.unparsed("original", original));

        final Component message = formatter.parse(alertMessage, builder.build());

        for (final Player player : proxyServer.getAllPlayers()) {
            if (Permission.NOTIFICATIONS.test(player)) {
                player.sendMessage(message);
            }
        }

        if (configurationContainer.get().getLog().warningLog()) {
            RegulatorCommandSource.INSTANCE.sendMessage(message);
        }

        if (fileLogger != null) {
            fileLogger.log(message);
        }
    }

    @Override
    protected void executeCommands(
            final ConfigurationContainer<Checks> checksContainer,
            final CheckResult.@NotNull DetectedResult result
    ) {
        final InfractionType type = result.infractionType();
        if (!(playerProvider.getAudience() instanceof final Player player)) {
            return;
        }

        final Checks.CommandsConfig config = checksContainer.get().getExecutable(type).getCommandsConfig();
        if (config.executeCommand() && getInfractions().getCount(type) % config.violationsRequired() == 0) {
            final String serverName = player.getCurrentServer()
                    .map(sv -> sv.getServerInfo().getName())
                    .orElse("");

            for (final String command : config.getCommandsToExecute()) {
                final String commandToExecute = command.replace("<player>", username())
                        .replace("<server>", serverName);
                proxyServer.getCommandManager()
                        .executeAsync(RegulatorCommandSource.INSTANCE, commandToExecute)
                        .handle((status, ex) -> {
                            if (ex != null) {
                                logger.warn("Error executing command {}", commandToExecute, ex);
                            } else if (!status) {
                                logger.warn("Error executing command {}", commandToExecute);
                            }
                            return null;
                        });
            }
        }
    }

    public void debug(String string, InfractionType detection) {
        if (logger.isDebugEnabled()) {
            logger.debug("User Detected: {}", username());
            logger.debug("Detection: {}", detection);
            logger.debug("String: {}", string);
        }
    }

    @Override
    public void onDetection(
            final ConfigurationContainer<Checks> checksContainer,
            final ConfigurationContainer<Messages> messagesContainer,
            final ConfigurationContainer<Configuration> configurationContainer,
            final Formatter formatter,
            final CheckResult.DetectedResult result,
            final String string
    ) {
        super.onDetection(checksContainer, messagesContainer, configurationContainer, formatter, result, string);
        debug(string, result.infractionType());
    }
}
