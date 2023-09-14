package io.github._4drian3d.chatregulator.plugin.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.plugin.config.Checks;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.config.Messages;
import io.github._4drian3d.chatregulator.plugin.placeholders.PlayerResolver;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import io.github._4drian3d.chatregulator.plugin.source.RegulatorCommandSource;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class InfractionPlayerImpl implements InfractionPlayer {
    private final Player player;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private Logger logger;
    @Inject
    private ConfigurationContainer<Checks> checksContainer;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private Formatter formatter;
    @Inject
    private RegulatorCommandSource regulatorSource;
    @Inject
    private FileLogger fileLogger;
    private final StringChainImpl commandChain = new StringChainImpl();
    private final StringChainImpl chatChain = new StringChainImpl();
    private final InfractionCount infractionCount = new InfractionCount();
    private final PlayerResolver resolver = new PlayerResolver(this);
    private boolean isOnline;
    private final String username;

    public InfractionPlayerImpl(@NotNull Player player, Injector injector) {
        this.player = requireNonNull(player);
        if (injector != null) {
            injector.injectMembers(this);
            injector.injectMembers(chatChain);
            injector.injectMembers(commandChain);
        }
        this.isOnline = true;
        this.username = player.getUsername();
    }

    @Override
    public @NotNull String username() {
        return this.username;
    }

    @Override
    public boolean isOnline() {
        return this.isOnline;
    }

    @Override
    public @NotNull StringChainImpl getChain(final @NotNull SourceType sourceType) {
        return switch (sourceType) {
            case CHAT -> chatChain;
            case COMMAND -> commandChain;
        };
    }

    public void isOnline(boolean status) {
        this.isOnline = status;
    }

    @Override
    public @NotNull InfractionCount getInfractions() {
        return infractionCount;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public @NotNull Audience audience() {
        return isOnline ? player : Audience.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final InfractionPlayerImpl other)) {
            return false;
        }
        return Objects.equals(other.username, this.username)
                && Objects.equals(other.getInfractions(), this.getInfractions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.username);
    }

    @Override
    public String toString() {
        return "InfractionPlayerImpl["
                + "name=" + this.username
                + ",online=" + this.isOnline
                + ",infraction-count=" + infractionCount
                + "]";
    }

    public boolean isAllowed(final InfractionType type) {
        return checksContainer.get().isEnabled(type) && !type.getBypassPermission().test(getPlayer());
    }

    private void sendWarningMessage(final CheckResult.DetectedResult result, final String detected) {
        final InfractionType type = result.infractionType();
        final String message = requireNonNull(messagesContainer.get().getWarning(type)).getWarningMessage();
        if (message.isBlank()) {
            return;
        }
        final TagResolver.Builder builder = TagResolver.builder();
        builder.resolver(getPlaceholders());

        builder.resolver(Placeholder.unparsed("infraction", detected));

        final TagResolver resolver = builder.build();
        final Checks.Warning configuration = checksContainer.get().getWarning(type);

        switch (configuration.getWarningType()) {
            case TITLE -> {
                final int index = message.indexOf(';');
                if (index == -1) {
                    sendSingleTitle(message, resolver, formatter);
                } else {
                    final String[] titleParts = message.split(";");
                    if (titleParts.length == 1) {
                        sendSingleTitle(titleParts[0], resolver, formatter);
                        return;
                    }
                    showTitle(
                            Title.title(
                                    formatter.parse(titleParts[0], this, resolver),
                                    formatter.parse(titleParts[1], this, resolver)
                            )
                    );
                }
            }
            case ACTIONBAR -> sendActionBar(formatter.parse(message, this, resolver));
            case MESSAGE -> sendMessage(formatter.parse(message, this, resolver));
        }
    }

    private void sendSingleTitle(String title, TagResolver resolver, Formatter formatter) {
        sendTitlePart(TitlePart.SUBTITLE, formatter.parse(title, resolver));
    }

    private void sendAlertMessage(final CheckResult.DetectedResult result, final String original) {
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
            regulatorSource.sendMessage(message);
        }

        if (fileLogger != null) {
            fileLogger.log(message);
        }
    }

    public @NotNull TagResolver getPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        builder.resolver(this.resolver);

        if (pluginManager.isLoaded("miniplaceholders")) {
            builder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this));
        }

        return builder.build();
    }

    public void sendResetMessage(Audience sender, InfractionType type) {
        if (sender instanceof InfractionPlayerImpl p && p.isOnline()) {
            sender = requireNonNull(p.getPlayer());
        }
        final TagResolver resolver = getPlaceholders();

        Messages.Reset messages = messagesContainer.get().getReset(type);
        sender.sendMessage(formatter.parse(messages.getResetMessage(), sender, resolver));
    }

    public void debug(String string, InfractionType detection) {
        if (logger.isDebugEnabled()) {
            logger.debug("User Detected: {}", username());
            logger.debug("Detection: {}", detection);
            logger.debug("String: {}", string);
        }
    }

    public void onDetection(final CheckResult.DetectedResult result, final String string) {
        this.sendWarningMessage(result, string);
        this.sendAlertMessage(result, string);
        this.getInfractions().addViolation(result.infractionType());
        this.executeCommands(result);
        this.debug(string, result.infractionType());
    }

    private void executeCommands(final @NotNull CheckResult.DetectedResult result) {
        final InfractionType type = result.infractionType();
        final Player player = getPlayer();
        if (player == null) {
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
                        .executeAsync(regulatorSource, commandToExecute)
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
}
