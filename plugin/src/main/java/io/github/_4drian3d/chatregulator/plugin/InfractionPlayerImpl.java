package io.github._4drian3d.chatregulator.plugin;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.event.ChatViolationEvent;
import io.github._4drian3d.chatregulator.api.event.CommandViolationEvent;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.api.result.Result;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static io.github._4drian3d.chatregulator.plugin.utils.Placeholders.integer;
import static java.util.Objects.requireNonNull;

public final class InfractionPlayerImpl implements InfractionPlayer {
    private final Player player;
    private final ChatRegulator plugin;
    private String preLastMessage;
    private String lastMessage;
    private String preLastCommand;
    private String lastCommand;
    private final InfractionCount infractionCount = new InfractionCount();
    private boolean isOnline;
    private final String username;
    private Instant timeSinceLastMessage;
    private Instant timeSinceLastCommand;

    public InfractionPlayerImpl(Player player, ChatRegulator plugin) {
        this.player = requireNonNull(player);
        this.plugin = plugin;
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.timeSinceLastMessage = Instant.now();
        this.timeSinceLastCommand = Instant.now();
        this.isOnline = true;
        this.username = player.getUsername();
    }

    public @NotNull String username() {
        return this.username;
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public void isOnline(boolean status) {
        this.isOnline = status;
    }

    public @NotNull String preLastMessage() {
        return preLastMessage;
    }

    public @NotNull String lastMessage() {
        return this.lastMessage;
    }

    public void lastMessage(final @NotNull String newLastMessage) {
        this.preLastMessage = this.lastMessage;
        this.lastMessage = newLastMessage;
        this.timeSinceLastMessage = Instant.now();
    }

    public @NotNull String preLastCommand() {
        return this.preLastCommand;
    }

    public @NotNull String lastCommand() {
        return this.lastCommand;
    }

    public void lastCommand(final @NotNull String newLastCommand) {
        this.preLastCommand = this.lastCommand;
        this.lastCommand = newLastCommand;
        this.timeSinceLastCommand = Instant.now();
    }

    public long getTimeSinceLastMessage() {
        return Duration.between(this.timeSinceLastMessage, Instant.now()).toMillis();
    }

    public long getTimeSinceLastCommand() {
        return Duration.between(this.timeSinceLastCommand, Instant.now()).toMillis();
    }

    @Override
    public @NotNull InfractionCount getInfractions() {
        return infractionCount;
    }

    /**
     * Obtain the original player
     * <p>
     * <strong>Check if the player is online with {@link #isOnline()}
     * if you are going to use this method
     * outside of a player event or command.</strong>
     *
     * @return the original {@link Player}
     */
    public @Nullable Player getPlayer() {
        return this.player;
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

    @Override
    public @NotNull Audience audience() {
        return isOnline ? player : Audience.empty();
    }

    public void sendWarningMessage(CheckResult result, InfractionType type) {
        final String message = plugin.getMessages().getWarning(type).getWarningMessage();
        final TagResolver placeholder = TagResolver.resolver(
                Placeholder.unparsed("infraction", result.toString()), //TODO: result.getInfrationWord o nose
                getPlaceholders());
        final var configuration = plugin.getConfig().getWarning(type);
        switch(configuration.getWarningType()) {
            case TITLE -> {
                int index = message.indexOf(';');
                if (index != -1) {
                    sendSingleTitle(message, placeholder, plugin.getFormatter());
                } else {
                    final String[] titleParts = message.split(";");
                    if (titleParts.length == 1) {
                        sendSingleTitle(titleParts[0], placeholder, plugin.getFormatter());
                        return;
                    }
                    showTitle(
                            Title.title(
                                    plugin.getFormatter().parse(
                                            titleParts[0],
                                            getPlayer(),
                                            placeholder),
                                    plugin.getFormatter().parse(
                                            titleParts[1],
                                            getPlayer(),
                                            placeholder)
                            )
                    );
                }
            }
            case ACTIONBAR -> sendActionBar(plugin.getFormatter().parse(message, getPlayer(), placeholder));
            case MESSAGE -> sendMessage(plugin.getFormatter().parse(message, getPlayer(), placeholder));
        }
    }

    private void sendSingleTitle(String title, TagResolver resolver, IFormatter formatter) {
        sendTitlePart(TitlePart.SUBTITLE, formatter.parse(title, resolver));
    }

    public void sendAlertMessage(final InfractionType type, final CheckResult result) {
        final var messages = plugin.getMessages().getAlert(type);
        final Component message = plugin.getFormatter().parse(
                messages.getAlertMessage(),
                TagResolver.resolver(
                        getPlaceholders(),
                        Placeholder.unparsed("string", result.toString())) //todo: get infractionstring
        );

        plugin.getProxy().getAllPlayers().forEach(player -> {
            if (Permission.NOTIFICATIONS.test(player)) {
                player.sendMessage(message);
            }
        });

        plugin.getProxy().getConsoleCommandSource().sendMessage(message);
    }

    public @NotNull TagResolver getPlaceholders() {
        final InfractionCount count = getInfractions();
        final TagResolver.Builder resolver = TagResolver.builder().resolvers(
                Placeholder.parsed("player", username()),
                Placeholder.parsed("name", username()),
                integer("flood", count.getCount(InfractionType.FLOOD)),
                integer("spam", count.getCount(InfractionType.SPAM)),
                integer("regular", count.getCount(InfractionType.REGULAR)),
                integer("unicode", count.getCount(InfractionType.UNICODE)),
                integer("caps", count.getCount(InfractionType.CAPS)),
                integer("command", count.getCount(InfractionType.BCOMMAND)),
                integer("syntax", count.getCount(InfractionType.SYNTAX))
        );

        return resolver.build();
    }

    public void sendResetMessage(Audience sender, InfractionType type) {
        if (sender instanceof InfractionPlayerImpl p && p.isOnline()) {
            sender = requireNonNull(p.getPlayer());
        }
        final TagResolver resolver = getPlaceholders();
        final IFormatter formatter = plugin.getFormatter();
        String resetMessage = requireNonNull(plugin.getMessages().getReset(type)).getResetMessage();
        sender.sendMessage(formatter.parse(resetMessage, sender, resolver));
    }

    public boolean callEvent(String string, InfractionType type, CheckResult result, SourceType source) {
        final var event = source == SourceType.COMMAND
                ? new CommandViolationEvent(this, type, result, string)
                : new ChatViolationEvent(this, type, result, string);
        return plugin.getProxy().getEventManager().fire(event)
                .thenApply(executed -> {
                    if (executed.getResult().isAllowed()) {
                        debug(string, type);
                        plugin.getStatistics().addInfractionCount(type);
                        sendWarningMessage(result, type);
                        sendAlertMessage(type, event.getDetectionResult());

                        getInfractions().addViolation(type);
                        executeCommands(type);
                        return true;
                    } else {
                        if (source == SourceType.COMMAND)
                            lastCommand(string);
                        else
                            lastMessage(string);
                        return false;
                    }
                }).join();
    }

    public void debug(String string, InfractionType detection) {
        Logger logger = plugin.getLogger();

        if (logger.isDebugEnabled()) {
            logger.debug("User Detected: {}", username());
            logger.debug("Detection: {}", detection);
            logger.debug("String: {}", string);
        }
    }

    // TODO: Cooldown Check
    boolean cooldown(Result result) {
        final Configuration.Spam config = plugin.getConfig().getSpamConfig();
        if (!result.isInfraction() || !config.getCooldownConfig().enabled()) {
            return false;
        }
        return getTimeSinceLastMessage() < config.getCooldownConfig().unit().toMillis(config.getCooldownConfig().limit());
    }

    public boolean isAllowed(InfractionType type) {
        return plugin.getConfig().isEnabled(type) && !type.getBypassPermission().test(getPlayer());
    }

    public void executeCommands(final @NotNull InfractionType type) {
        final Player player = getPlayer();
        if (player == null) {
            return;
        }

        final Configuration.CommandsConfig config = plugin.getConfig().getExecutable(type).getCommandsConfig();
        if (config.executeCommand() && getInfractions().getCount(type) % config.violationsRequired() == 0) {
            final String serverName = player.getCurrentServer().map(sv -> sv.getServerInfo().getName()).orElse("");
            config.getCommandsToExecute().forEach(cmd -> {
                final String command = cmd.replace("<player>", username())
                        .replace("<server>", serverName);
                plugin.getProxy().getCommandManager()
                        .executeAsync(plugin.source(), command)
                        .handleAsync((status, ex) -> {
                            if (ex != null) {
                                plugin.getLogger().warn("Error executing command {}", command, ex);
                            } else if (!status) {
                                plugin.getLogger().warn("Error executing command {}", command);
                            }
                            return null;
                        });
            });
        }
    }
}
