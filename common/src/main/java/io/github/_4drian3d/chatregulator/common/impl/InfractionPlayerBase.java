package io.github._4drian3d.chatregulator.common.impl;

import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.common.placeholders.PlayerResolver;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.Formatter;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.MiniPlaceholderFormatter;
import io.github._4drian3d.chatregulator.common.player.PlayerWrapper;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class InfractionPlayerBase implements InfractionPlayer {
    protected final PlayerWrapper playerProvider;

    private final InfractionCount infractionCount = new InfractionCount();
    private final PlayerResolver resolver = new PlayerResolver(this);
    protected final String username;

    protected InfractionPlayerBase(UUID playerUUID, Function<UUID, Audience> playerConverter) {
        this.playerProvider = new PlayerWrapper(playerUUID, playerConverter);
        this.username = playerProvider.get(Identity.NAME).orElseThrow();
    }

    @Override
    public @NotNull String username() {
        return this.username;
    }

    @Override
    public boolean isOnline() {
        return this.playerProvider.isOnline();
    }

    @Override
    public @NotNull InfractionCount getInfractions() {
        return infractionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final InfractionPlayerBase other)) {
            return false;
        }
        return Objects.equals(other.username, this.username)
                && Objects.equals(other.getInfractions(), this.getInfractions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.playerProvider.playerUUID(), this.username);
    }

    @Override
    public String toString() {
        return "InfractionPlayerBase["
                + "name=" + this.username
                + ",uuid=" + this.playerProvider.playerUUID()
                + ",infraction-count=" + infractionCount
                + "]";
    }

    public boolean canBeTestBy(ConfigurationContainer<Checks> checksContainer, final InfractionType type) {
        if (!playerProvider.isOnline()) {
            throw new IllegalStateException("Player cannot be accessed while is not online");
        }
        return checksContainer.get().isEnabled(type) && !type.getBypassPermission().test(playerProvider.audience());
    }

    private void sendWarningMessage(
            final ConfigurationContainer<Checks> checksContainer,
            final ConfigurationContainer<Messages> messagesContainer,
            final Formatter formatter,
            final CheckResult.DetectedResult result,
            final String detected
    ) {
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

    protected abstract void sendAlertMessage(
            final ConfigurationContainer<Checks> checksContainer,
            final ConfigurationContainer<Messages> messagesContainer,
            final ConfigurationContainer<Configuration> configurationContainer,
            final Formatter formatter,
            final CheckResult.DetectedResult result,
            final String original
    );

    public @NotNull TagResolver getPlaceholders() {
        final TagResolver.Builder builder = TagResolver.builder();
        builder.resolver(this.resolver);

        if (MiniPlaceholderFormatter.MINIPLACEHOLDERS_INSTALLED) {
            builder.resolver(MiniPlaceholders.audienceGlobalPlaceholders());
        }

        return builder.build();
    }

    public void sendResetMessage(
            final ConfigurationContainer<Messages> messagesContainer,
            final Formatter formatter,
            Audience sender,
            final InfractionType type
    ) {
        if (sender instanceof InfractionPlayerBase p && p.isOnline()) {
            sender = requireNonNull(p.audience());
        }
        final TagResolver resolver = getPlaceholders();

        Messages.Reset messages = messagesContainer.get().getReset(type);
        sender.sendMessage(formatter.parse(messages.getResetMessage(), sender, resolver));
    }

    public void onDetection(
            final ConfigurationContainer<Checks> checksContainer,
            final ConfigurationContainer<Messages> messagesContainer,
            final ConfigurationContainer<Configuration> configurationContainer,
            final Formatter formatter,
            final CheckResult.DetectedResult result,
            final String string
    ) {
        this.sendWarningMessage(checksContainer, messagesContainer, formatter, result, string);
        this.sendAlertMessage(checksContainer, messagesContainer, configurationContainer, formatter, result, string);
        this.getInfractions().addViolation(result.infractionType());
        this.executeCommands(checksContainer, result);
    }

    protected abstract void executeCommands(
            final ConfigurationContainer<Checks> checksContainer,
            final @NotNull CheckResult.DetectedResult result
    );

    @Override
    public @NotNull Audience audience() {
        return playerProvider;
    }
}