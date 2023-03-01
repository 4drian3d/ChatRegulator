package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.Components;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Collection;
import java.util.Optional;

public final class RegulatorCommand {
    @Inject
    private ChatRegulator plugin;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private CommandManager commandManager;

    public void registerCommand() {
        final var commandBuilder = literal("chatregulator")
                .requires(Permission.COMMAND)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                    plugin.getMessages().getGeneralMessages().getInfoMessage(),
                                    Placeholder.unparsed("command", cmd.getInput())));
                    return Command.SINGLE_SUCCESS;
                })
                .then(statsCommand())
                .then(playerCommand())
                .then(resetCommand())
                .then(clearCommand())
                .then(reloadCommand());
        applyHelp(commandBuilder);

        final BrigadierCommand bCommand = new BrigadierCommand(commandBuilder);
        final CommandMeta meta = commandManager
                .metaBuilder(bCommand)
                .aliases("chatr", "cregulator")
                .plugin(plugin)
                .build();
        commandManager.register(meta, bCommand);
    }

    private void sendLines(Audience sender, Collection<String> lines, TagResolver resolver, IFormatter formatter) {
        lines.forEach(ln -> sender.sendMessage(formatter.parse(ln, sender, resolver)));
    }

    private static final TagResolver regulatorResolver = Placeholder.unparsed("command", "chatregulator");

    private void applyHelp(LiteralArgumentBuilder<CommandSource> builder) {
        var command = literal("help")
                .requires(Permission.COMMAND_HELP)
                .executes(cmd -> {
                    var helpMessages = plugin.getMessages().getGeneralMessages().getHelpMessages();
                    sendLines(cmd.getSource(), helpMessages.getMainHelp(), regulatorResolver, plugin.getFormatter());
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("clear")
                        .executes(cmd -> {
                            var helpMessages = plugin.getMessages().getGeneralMessages().getHelpMessages();
                            sendLines(cmd.getSource(), helpMessages.getClearHelp(), regulatorResolver, plugin.getFormatter());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(literal("reset")
                        .executes(cmd -> {
                            var helpMessages = plugin.getMessages().getGeneralMessages().getHelpMessages();
                            sendLines(cmd.getSource(), helpMessages.getResethelp(), regulatorResolver, plugin.getFormatter());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(literal("player")
                        .executes(cmd -> {
                            var helpMessages = plugin.getMessages().getGeneralMessages().getHelpMessages();
                            sendLines(cmd.getSource(), helpMessages.getPlayerHelp(), regulatorResolver, plugin.getFormatter());
                            return Command.SINGLE_SUCCESS;
                        })
                ).build();
        var redirect = literal("info").redirect(command);
        builder.then(command).then(redirect);
    }

    private LiteralCommandNode<CommandSource> statsCommand() {
        return literal("stats")
                .requires(Permission.COMMAND_STATS)
                .executes(cmd -> {
                    final TagResolver resolver;
                    if(cmd.getSource() instanceof Player player) {
                        InfractionPlayerImpl infractor = plugin.getPlayerManager()
                                .getPlayer(player.getUniqueId());
                        resolver = infractor.getPlaceholders();
                    } else {
                        resolver = plugin.getStatistics().getPlaceholders();
                    }
                    sendLines(
                            cmd.getSource(),
                            plugin.getMessages().getGeneralMessages().getStatsFormat(),
                            resolver,
                            plugin.getFormatter());
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private LiteralCommandNode<CommandSource> playerCommand() {
        return literal("player")
                .requires(Permission.COMMAND_PLAYER)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                    plugin.getMessages().getGeneralMessages().noArgument(),
                                    cmd.getSource()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(argument("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            plugin.getChatPlayers()
                                    .entrySet()
                                    .stream()
                                    .limit(plugin.getConfig().getGeneralConfig().tabCompleteLimit())
                                    .forEach((entry) -> builder.suggest(
                                            entry.getValue().username(),
                                            VelocityBrigadierMessage.tooltip(
                                                    plugin.getFormatter().parse(
                                                            plugin.getMessages().getGeneralMessages().getPlayerSuggestionsFormat(),
                                                            entry.getValue(),
                                                            Placeholder.unparsed("player", entry.getValue().username())
                                                    )
                                            )));
                            return builder.buildFuture();
                        })
                        .executes(cmd -> {
                            String arg = cmd.getArgument("player", String.class);
                            CommandSource source = cmd.getSource();

                            proxyServer.getPlayer(arg).ifPresentOrElse(player -> {
                                final InfractionPlayerImpl infractionPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
                                TagResolver placeholders = infractionPlayer.getPlaceholders();
                                sendLines(source, plugin.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                            }, () -> {
                                Optional<InfractionPlayerImpl> opt = plugin.getChatPlayers()
                                        .values()
                                        .stream()
                                        .filter(p -> p.username().equals(arg))
                                        .findAny();
                                if (opt.isPresent()) {
                                    TagResolver placeholders = opt.get().getPlaceholders();
                                    sendLines(source, plugin.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                                } else {
                                    source.sendMessage(plugin.getFormatter().parse(
                                            plugin.getMessages().getGeneralMessages().playerNotFound(),
                                            Placeholder.unparsed("player", arg)
                                    ));
                                }
                            });
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

    private LiteralCommandNode<CommandSource> resetCommand() {
        return literal("reset")
                .requires(Permission.COMMAND_RESET)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                    plugin.getMessages().getGeneralMessages().noArgument(),
                                    cmd.getSource()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(argument("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            plugin.getChatPlayers().forEach((uuid, p) -> builder.suggest(p.username(), VelocityBrigadierMessage.tooltip(
                                    plugin.getFormatter().parse(
                                            plugin.getMessages().getGeneralMessages().getPlayerSuggestionsFormat(),
                                            p,
                                            Placeholder.unparsed("player", p.username())
                                    )
                            )));
                            return builder.buildFuture();
                        })
                        .executes(cmd -> {
                            final String arg = cmd.getArgument("player", String.class);
                            final Player player = proxyServer.getPlayer(arg).orElse(null);
                            if (player == null) {
                                cmd.getSource().sendMessage(
                                        plugin.getFormatter().parse(
                                                plugin.getMessages().getGeneralMessages().playerNotFound(),
                                                cmd.getSource(), Placeholder.unparsed("player", arg)
                                        )
                                );
                                return Command.SINGLE_SUCCESS;
                            }
                            final InfractionPlayerImpl p = plugin.getPlayerManager().getPlayer(player.getUniqueId());

                            p.getInfractions().resetViolations(
                                    InfractionType.SPAM,
                                    InfractionType.FLOOD,
                                    InfractionType.REGULAR,
                                    InfractionType.BCOMMAND,
                                    InfractionType.UNICODE,
                                    InfractionType.CAPS,
                                    InfractionType.SYNTAX
                            );
                            p.sendResetMessage(cmd.getSource(), InfractionType.NONE);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(resetWithPlayerSubcommand("infractions", InfractionType.REGULAR, Permission.COMMAND_RESET_REGULAR))
                        .then(resetWithPlayerSubcommand("regular", InfractionType.REGULAR, Permission.COMMAND_RESET_REGULAR))
                        .then(resetWithPlayerSubcommand("flood", InfractionType.FLOOD, Permission.COMMAND_RESET_FLOOD))
                        .then(resetWithPlayerSubcommand("spam", InfractionType.SPAM, Permission.COMMAND_RESET_SPAM))
                        .then(resetWithPlayerSubcommand("command", InfractionType.BCOMMAND, Permission.COMMAND_RESET_BCOMMAND))
                        .then(resetWithPlayerSubcommand("unicode", InfractionType.UNICODE, Permission.COMMAND_RESET_UNICODE))
                        .then(resetWithPlayerSubcommand("caps", InfractionType.CAPS, Permission.COMMAND_RESET_CAPS))
                        .then(resetWithPlayerSubcommand("syntax", InfractionType.SYNTAX, Permission.COMMAND_RESET_SYNTAX))
                )
                .build();
    }

    private LiteralCommandNode<CommandSource> resetWithPlayerSubcommand(String subcommand, InfractionType type, Permission resetPermission) {
        return literal(subcommand)
                .requires(resetPermission)
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    InfractionPlayerImpl p = plugin.getPlayerManager().getPlayer(arg);
                    if (p == null) {
                        cmd.getSource().sendMessage(
                                plugin.getFormatter().parse(
                                        plugin.getMessages().getGeneralMessages().playerNotFound(),
                                        cmd.getSource(),
                                        Placeholder.unparsed("player", arg)
                                )
                        );
                        return Command.SINGLE_SUCCESS;
                    }
                    p.getInfractions().resetViolations(type);
                    p.sendResetMessage(cmd.getSource(), type);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    private LiteralCommandNode<CommandSource> clearCommand() {
        return literal("clear")
                .requires(Permission.COMMAND_CLEAR)
                .executes(cmd -> {
                    proxyServer.sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                    plugin.getMessages().getClearMessages().getGlobalMessage()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("server")
                        .requires(Permission.COMMAND_CLEAR_SERVER)
                        .executes(cmd -> {
                            if (cmd.getSource() instanceof final Player player) {
                                player.getCurrentServer().ifPresent(playerServer -> {
                                    playerServer.getServer().sendMessage(Components.SPACES_COMPONENT);
                                    player.sendMessage(plugin.getFormatter().parse(
                                            plugin.getMessages().getClearMessages().getServerMessage(),
                                            player,
                                            Placeholder.unparsed("server", playerServer.getServerInfo().getName())
                                    ));
                                });
                            } else {
                                cmd.getSource().sendMessage(
                                        plugin.getFormatter().parse(
                                                plugin.getMessages().getGeneralMessages().noArgument(),
                                                cmd.getSource()
                                        )
                                );
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(argument("server", StringArgumentType.word())
                                .suggests((arg, builder) -> {
                                    proxyServer.getAllServers().forEach(sv -> builder.suggest(sv.getServerInfo().getName()));
                                    return builder.buildFuture();
                                })
                                .executes(cmd -> {
                                    String arg = cmd.getArgument("server", String.class);
                                    TagResolver serverPlaceholder = Placeholder.unparsed("server", arg);
                                    proxyServer.getServer(arg).ifPresentOrElse(serverObjective -> {
                                        serverObjective.sendMessage(Components.SPACES_COMPONENT);
                                        cmd.getSource().sendMessage(
                                                plugin.getFormatter().parse(
                                                        plugin.getMessages().getClearMessages().getServerMessage(),
                                                        cmd.getSource(),
                                                        serverPlaceholder
                                                )
                                        );
                                    }, () -> cmd.getSource().sendMessage(
                                            plugin.getFormatter().parse(
                                                    plugin.getMessages().getClearMessages().getNotFoundServerMessage(),
                                                    cmd.getSource(),
                                                    serverPlaceholder
                                            )
                                    ));
                                    return Command.SINGLE_SUCCESS;
                                }).build()
                        )
                )
                .then(literal("player")
                        .requires(Permission.COMMAND_CLEAR_PLAYER)
                        .executes(cmd -> {
                            cmd.getSource().sendMessage(
                                    plugin.getFormatter().parse(
                                            plugin.getMessages().getGeneralMessages().noArgument(),
                                            cmd.getSource()
                                    )
                            );
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                        .then(argument("player", StringArgumentType.word())
                                .suggests((arg, builder) -> {
                                    plugin.getChatPlayers().forEach((uuid, p) -> builder.suggest(p.username(), VelocityBrigadierMessage.tooltip(
                                            plugin.getFormatter().parse(
                                                    plugin.getMessages().getGeneralMessages().getPlayerSuggestionsFormat(),
                                                    p,
                                                    Placeholder.unparsed("player", p.username())
                                            )
                                    )));
                                    return builder.buildFuture();
                                })
                                .executes(cmd -> {
                                    String arg = cmd.getArgument("player", String.class);
                                    TagResolver serverPlaceholder = Placeholder.unparsed("player", arg);
                                    proxyServer.getPlayer(arg).ifPresentOrElse(playerObjective -> {
                                        playerObjective.sendMessage(Components.SPACES_COMPONENT);
                                        cmd.getSource().sendMessage(
                                                plugin.getFormatter().parse(
                                                        plugin.getMessages().getClearMessages().getPlayerMessage(),
                                                        cmd.getSource(),
                                                        serverPlaceholder
                                                )
                                        );
                                    }, () -> cmd.getSource().sendMessage(
                                            plugin.getFormatter().parse(
                                                    plugin.getMessages().getClearMessages().getNotFoundServerMessage(),
                                                    cmd.getSource(),
                                                    serverPlaceholder
                                            )
                                    ));
                                    return Command.SINGLE_SUCCESS;
                                }).build()
                        ).build()
                )
                .then(literal("all")
                        .executes(cmd -> {
                            proxyServer.sendMessage(Components.SPACES_COMPONENT);
                            cmd.getSource().sendMessage(
                                    plugin.getFormatter().parse(
                                            plugin.getMessages().getClearMessages().getGlobalMessage(),
                                            cmd.getSource()
                                    )
                            );
                            return Command.SINGLE_SUCCESS;
                        }).build()
                ).build();

    }

    private LiteralCommandNode<CommandSource> reloadCommand() {
        return LiteralArgumentBuilder
                .<CommandSource>literal("reload")
                .requires(Permission.COMMAND_RELOAD)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                    plugin.getMessages().getGeneralMessages().getReloadMessage()
                            )
                    );
                    plugin.reloadConfig();
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    public static LiteralArgumentBuilder<CommandSource> literal(String command) {
        return LiteralArgumentBuilder.literal(command);
    }

    public static <E> RequiredArgumentBuilder<CommandSource, E> argument(String command, ArgumentType<E> argumentType) {
        return RequiredArgumentBuilder.argument(command, argumentType);
    }
}


