package io.github._4drian3d.chatregulator.plugin;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.event.ChatViolationEvent;
import io.github._4drian3d.chatregulator.api.event.CommandViolationEvent;
import io.github._4drian3d.chatregulator.api.event.ViolationEvent;
import io.github._4drian3d.chatregulator.api.result.IReplaceable;
import io.github._4drian3d.chatregulator.api.result.PatternResult;
import io.github._4drian3d.chatregulator.api.result.ReplaceableResult;
import io.github._4drian3d.chatregulator.api.result.Result;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import io.github._4drian3d.chatregulator.plugin.wrapper.EventWrapper;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static io.github._4drian3d.chatregulator.plugin.utils.Placeholders.integer;

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

    InfractionPlayerImpl(Player player, ChatRegulator plugin) {
        this.player = Objects.requireNonNull(player);
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

    public void sendWarningMessage(Result result, InfractionType type) {
        final String message = plugin.getMessages().getWarning(type).getWarningMessage();
        final TagResolver placeholder = TagResolver.resolver(
                Placeholder.unparsed("infraction", result.getInfractionString()),
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

    public void sendAlertMessage(final InfractionType type, final Result result) {
        final var messages = plugin.getMessages().getAlert(type);
        final Component message = plugin.getFormatter().parse(
                messages.getAlertMessage(),
                TagResolver.resolver(
                        getPlaceholders(),
                        Placeholder.unparsed("string", result.getInfractionString()))
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
                Placeholder.unparsed("player", username()),
                Placeholder.unparsed("name", username()),
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
            sender = p.getPlayer();
        }
        final TagResolver resolver = getPlaceholders();
        final IFormatter formatter = plugin.getFormatter();
        String resetMessage = plugin.getMessages().getReset(type).getResetMessage();
        sender.sendMessage(formatter.parse(resetMessage, sender, resolver));
    }

    public boolean callEvent(String string, InfractionType type, Result result, SourceType source) {
        final var event = source == SourceType.COMMAND
                ? new CommandViolationEvent(this, type, result, string)
                : new ChatViolationEvent(this, type, result, string);
        return plugin.getProxy().getEventManager().fire(event)
                .exceptionallyAsync(ex -> {
                    plugin.getLogger().error("An Error occurred on Violation Event call", ex);
                    return new ViolationEvent(this, type, new Result(string, false)) {
                        @Override
                        public GenericResult getResult() {
                            return GenericResult.denied();
                        }
                    };
                })
                .thenApplyAsync(executed -> {
                    if (executed.getResult().isAllowed()) {
                        debug(string, type, result);
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

    public void debug(String string, InfractionType detection, Result result) {
        Logger logger = plugin.getLogger();

        if (logger.isDebugEnabled()) {
            logger.debug("User Detected: {}", username());
            logger.debug("Detection: {}", detection);
            logger.debug("String: {}", string);
            if (result instanceof final PatternResult patternResult) {
                final Pattern pattern = patternResult.getPattern();
                if (pattern != null)
                    logger.debug("Pattern: {}", pattern.pattern());
            }
        }
    }

    public boolean unicode(AtomicReference<String> string, EventWrapper<?> event) {
        UnicodeCheck check = UnicodeCheck.builder().build();
        return isAllowed(InfractionType.UNICODE) && check.check(string.get())
                .exceptionallyAsync(e -> {
                    plugin.getLogger().error("An Error occurred on Unicode Check", e);
                    return new Result("", false);
                }).thenApplyAsync(result -> {
                    if (result.isInfraction() && callEvent(string.get(), InfractionType.UNICODE, result, event.source())) {
                        if (plugin.getConfig().getUnicodeConfig().isBlockable()) {
                            event.cancel();
                            event.resume();
                            return true;
                        }
                        if (result instanceof final ReplaceableResult replaceable) {
                            string.set(replaceable.replaceInfraction());
                            event.setString(string.get());
                        }
                    }
                    return false;
                }).join();
    }

    public boolean caps(AtomicReference<String> string, EventWrapper<?> event) {
        CapsCheck check = CapsCheck.builder().build();
        return isAllowed(InfractionType.CAPS) && check.check(string.get())
                .exceptionallyAsync(e -> {
                    plugin.getLogger().error("An Error occurred on Caps Check", e);
                    return new Result("", false);
                }).thenApplyAsync(result -> {
                    if (result.isInfraction() && callEvent(string.get(), InfractionType.CAPS, result, event.source())) {
                        if (plugin.getConfig().getCapsConfig().isBlockable()) {
                            event.cancel();
                            event.resume();
                            return true;
                        }
                        if (result instanceof final IReplaceable replaceable) {
                            string.set(replaceable.replaceInfraction());
                            event.setString(string.get());
                        }
                    }
                    return false;
                }).join();
    }

    public boolean flood(AtomicReference<String> string, EventWrapper<?> event) {
        FloodCheck check = FloodCheck.builder().build();
        return isAllowed(InfractionType.FLOOD) && check.check(string.get())
                .exceptionallyAsync(e -> {
                    plugin.getLogger().error("An Error occurred on Flood Check", e);
                    return new Result("", false);
                }).thenApplyAsync(result -> {
                    if (result.isInfraction() && callEvent(string.get(), InfractionType.FLOOD, result, event.source())) {
                        if (plugin.getConfig().getFloodConfig().isBlockable()) {
                            event.cancel();
                            event.resume();
                            return true;
                        }
                        if (result instanceof final IReplaceable replaceable) {
                            string.set(replaceable.replaceInfraction());
                            event.setString(string.get());
                        }
                    }
                    return false;
                }).join();
    }

    public boolean regular(AtomicReference<String> string, EventWrapper<?> event) {
        InfractionCheck check = InfractionCheck.builder().build();
        return isAllowed(InfractionType.REGULAR) && check.check(string.get()).exceptionallyAsync(e -> {
            plugin.getLogger().error("An Error occurred on Regular Infraction Check", e);
            return new Result("", false);
        }).thenApplyAsync(result -> {
            if (result.isInfraction() && callEvent(string.get(), InfractionType.REGULAR, result, event.source())) {
                if (plugin.getConfig().getInfractionsConfig().isBlockable()) {
                    event.cancel();
                    event.resume();
                    return true;
                }
                if (result instanceof final IReplaceable replaceable) {
                    string.set(replaceable.replaceInfraction());
                    event.setString(string.get());
                }
            }
            return false;
        }).join();
    }

    public boolean spam(AtomicReference<String> string, EventWrapper<?> event) {
        if (isAllowed(InfractionType.SPAM)) {
            final Result result = SpamCheck.createCheck(this, string.get(), event.source())
                    .exceptionallyAsync(e -> {
                        plugin.getLogger().error("An Error ocurred on Spam Check", e);
                        return new Result("", false);
                    }).join();
            if (cooldown(result)
                    && callEvent(string.get(), InfractionType.SPAM, result, event.source())
            ) {
                event.cancel();
                event.resume();
                return true;
            }
        }
        return false;
    }

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
            final String servername = player.getCurrentServer().map(sv -> sv.getServerInfo().getName()).orElse("");
            config.getCommandsToExecute().forEach(cmd -> {
                final String command = cmd.replace("<player>", username())
                        .replace("<server>", servername);
                plugin.getProxy().getCommandManager()
                        .executeAsync(plugin.source(), command)
                        .handleAsync((status, ex) -> {
                            if (ex != null) {
                                plugin.getLogger().warn("Error executing command {}", command, ex);
                            } else if (!status.booleanValue()) {
                                plugin.getLogger().warn("Error executing command {}", command);
                            }
                            return null;
                        });
            });
        }
    }

    public boolean syntax(String string, EventWrapper<?> event) {
        Result result = SyntaxCheck.createCheck(string)
                .exceptionallyAsync(e -> {
                    plugin.getLogger().error("An Error occurred on Syntax Check", e);
                    return new Result("", false);
                }).join();
        if (isAllowed(InfractionType.SYNTAX)
                && callEvent(string,
                InfractionType.SYNTAX,
                result,
                SourceType.COMMAND
        )
        ) {
            event.cancel();
            event.resume();
            return true;
        }
        return false;
    }

    public boolean blockedCommands(String string, EventWrapper<?> event) {
        if (isAllowed(InfractionType.BCOMMAND)) {
            CommandCheck check = CommandCheck.builder()
                    .blockedCommands(plugin.getBlacklist().getBlockedCommands())
                    .build();
            var result = check.check(string).exceptionallyAsync(e -> {
                plugin.getLogger().error("An Error occurred on Blocked Commands Check", e);
                return new Result("", false);
            }).join();

            if (callEvent(string, InfractionType.BCOMMAND, result, SourceType.COMMAND)) {
                event.cancel();
                event.resume();
                return true;
            }
        }
        return false;
    }


}
