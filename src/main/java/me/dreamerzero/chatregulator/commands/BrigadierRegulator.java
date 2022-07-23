package me.dreamerzero.chatregulator.commands;

import java.util.Collection;
import java.util.Optional;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permission;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class BrigadierRegulator {
    private BrigadierRegulator(){}

    public static void registerCommand(ChatRegulator plugin){
        LiteralCommandNode<CommandSource> commandNode = LiteralArgumentBuilder
            .<CommandSource>literal("chatregulator")
            .requires(Permission.COMMAND)
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        plugin.getMessages().getGeneralMessages().getInfoMessage(),
                        Placeholder.unparsed("command", cmd.getInput())));
                return 1;
            })
            .then(infoSubCommand("help", plugin))
            .then(infoSubCommand("info", plugin))
            .then(statsCommand("stats", plugin))
            .then(playerCommand("player", plugin))
            .then(resetCommand("reset", plugin))
            .then(clearCommand("clear", plugin))
            .then(reloadCommand("reload", plugin))
            .build();

        ProxyServer proxy = plugin.getProxy();
        BrigadierCommand bCommand = new BrigadierCommand(commandNode);
        CommandMeta meta = proxy.getCommandManager().metaBuilder(bCommand).aliases("chatr", "cregulator").build();
        proxy.getCommandManager().register(meta, bCommand);
    }

    private static void sendLines(Audience sender, Collection<String> lines, TagResolver resolver, IFormatter formatter){
        lines.forEach(ln -> sender.sendMessage(formatter.parse(ln, sender, resolver)));
    }

    private static final TagResolver chatrCommand = Placeholder.unparsed("command", "chatregulator");

    private static LiteralCommandNode<CommandSource> infoSubCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_HELP)
            .executes(cmd -> {
                sendLines(cmd.getSource(), plugin.getMessages().getGeneralMessages().getHelpMessages().getMainHelp(), chatrCommand, plugin.getFormatter());
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("clear")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), plugin.getMessages().getGeneralMessages().getHelpMessages().getClearHelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                }).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("reset")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), plugin.getMessages().getGeneralMessages().getHelpMessages().getResethelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                })
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), plugin.getMessages().getGeneralMessages().getHelpMessages().getPlayerHelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                })
            ).build();
    }

    private static LiteralCommandNode<CommandSource> statsCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_STATS)
            .executes(cmd -> {
                TagResolver resolver = cmd.getSource() instanceof Player player
                    ? plugin.placeholders().getPlaceholders(InfractionPlayer.get(player))
                    : plugin.placeholders().getGlobalPlaceholders();
                sendLines(
                    cmd.getSource(),
                    plugin.getMessages().getGeneralMessages().getStatsFormat(),
                    resolver,
                    plugin.getFormatter());
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> playerCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_PLAYER)
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        plugin.getMessages().getGeneralMessages().noArgument(),
                        cmd.getSource()
                    )
                );
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
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
                    String arg = cmd.getArgument("player", String.class);
                    CommandSource source = cmd.getSource();
                    Optional<Player> optionalPlayer = plugin.getProxy().getPlayer(arg);
                    if(optionalPlayer.isPresent()){
                        InfractionPlayer infractionPlayer = InfractionPlayer.get(optionalPlayer.get());
                        TagResolver placeholders = plugin.placeholders().getPlaceholders(infractionPlayer);
                        sendLines(source, plugin.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                    } else {
                        Optional<InfractionPlayer> opt = plugin.getChatPlayers().values().stream().filter(p -> p.username().equals(arg)).findAny();
                        if(opt.isPresent()){
                            TagResolver placeholders = plugin.placeholders().getPlaceholders(opt.get());
                            sendLines(source, plugin.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                        } else {
                            source.sendMessage(plugin.getFormatter().parse(
                                plugin.getMessages().getGeneralMessages().playerNotFound(),
                                Placeholder.unparsed("player", arg)
                            ));
                        }
                    }
                    return 1;
                }))
            .build();
    }

    private static LiteralCommandNode<CommandSource> resetCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_RESET)
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        plugin.getMessages().getGeneralMessages().noArgument(),
                        cmd.getSource()
                    )
                );
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
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
                    final InfractionPlayer p = InfractionPlayer.get(arg);
                    if(p == null){
                        cmd.getSource().sendMessage(
                            plugin.getFormatter().parse(
                                plugin.getMessages().getGeneralMessages().playerNotFound(),
                                cmd.getSource(), Placeholder.unparsed("player", arg)
                            )
                        );
                        return 1;
                    }
                    resetAll(p, cmd.getSource(), plugin);
                    return 1;
                })
                .then(resetWithPlayerSubcommand("infractions", InfractionType.REGULAR, Permission.COMMAND_RESET_REGULAR, plugin))
                .then(resetWithPlayerSubcommand("regular", InfractionType.REGULAR, Permission.COMMAND_RESET_REGULAR, plugin))
                .then(resetWithPlayerSubcommand("flood", InfractionType.FLOOD, Permission.COMMAND_RESET_FLOOD, plugin))
                .then(resetWithPlayerSubcommand("spam", InfractionType.SPAM, Permission.COMMAND_RESET_SPAM, plugin))
                .then(resetWithPlayerSubcommand("command", InfractionType.BCOMMAND, Permission.COMMAND_RESET_BCOMMAND, plugin))
                .then(resetWithPlayerSubcommand("unicode", InfractionType.UNICODE, Permission.COMMAND_RESET_UNICODE, plugin))
                .then(resetWithPlayerSubcommand("caps", InfractionType.CAPS, Permission.COMMAND_RESET_CAPS, plugin))
                .then(resetWithPlayerSubcommand("syntax", InfractionType.SYNTAX, Permission.COMMAND_RESET_SYNTAX, plugin))
                )
            .build();
    }

    private static void resetAll(InfractionPlayer player, Audience source, ChatRegulator plugin){
        player.getViolations().resetViolations(
            InfractionType.SPAM,
            InfractionType.FLOOD,
            InfractionType.REGULAR,
            InfractionType.BCOMMAND,
            InfractionType.UNICODE,
            InfractionType.CAPS,
            InfractionType.SYNTAX
        );
        ConfigManager.sendResetMessage(source, InfractionType.NONE, player, plugin);
    }

    private  static LiteralCommandNode<CommandSource> resetWithPlayerSubcommand(String subcommand, InfractionType type, Permission resetPermission, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(subcommand)
            .requires(resetPermission)
            .executes(cmd -> {
                String arg = cmd.getArgument("player", String.class);
                InfractionPlayer p = InfractionPlayer.get(arg);
                if(p == null){
                    cmd.getSource().sendMessage(
                        plugin.getFormatter().parse(
                            plugin.getMessages().getGeneralMessages().playerNotFound(),
                            cmd.getSource(),
                            Placeholder.unparsed("player", arg)
                        )
                    );
                    return 1;
                }
                p.getViolations().resetViolations(type);
                ConfigManager.sendResetMessage(cmd.getSource(), type, p, plugin);
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> clearCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_CLEAR)
            .executes(cmd -> {
                plugin.getProxy().sendMessage(Components.SPACES_COMPONENT);
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        plugin.getMessages().getClearMessages().getGlobalMessage()
                    )
                );
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("server")
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
                    return 1;
                })
                .then(RequiredArgumentBuilder
                    .<CommandSource, String>argument("server", StringArgumentType.word())
                    .suggests((arg, builder) -> {
                        plugin.getProxy().getAllServers().forEach(sv -> builder.suggest(sv.getServerInfo().getName()));
                        return builder.buildFuture();
                    })
                    .executes(cmd -> {
                        String arg = cmd.getArgument("server", String.class);
                        TagResolver serverPlaceholder = Placeholder.unparsed("server", arg);
                        plugin.getProxy().getServer(arg).ifPresentOrElse(serverObjetive -> {
                            serverObjetive.sendMessage(Components.SPACES_COMPONENT);
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
                        return 1;
                    }).build()
                )
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .requires(Permission.COMMAND_CLEAR_PLAYER)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                        plugin.getFormatter().parse(
                            plugin.getMessages().getGeneralMessages().noArgument(),
                            cmd.getSource()
                        )
                    );
                    return 1;
                })
                .then(RequiredArgumentBuilder
                    .<CommandSource, String>argument("player", StringArgumentType.word())
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
                        plugin.getProxy().getPlayer(arg).ifPresentOrElse(playerObjetive -> {
                            playerObjetive.sendMessage(Components.SPACES_COMPONENT);
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
                        return 1;
                    }).build()
                ).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("all")
                .executes(cmd -> {
                    plugin.getProxy().sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(
                        plugin.getFormatter().parse(
                            plugin.getMessages().getClearMessages().getGlobalMessage(),
                            cmd.getSource()
                        )
                    );
                    return 1;
                }).build()
            ).build();

    }

    private static LiteralCommandNode<CommandSource> reloadCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(Permission.COMMAND_RELOAD)
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        plugin.getMessages().getGeneralMessages().getReloadMessage()
                    )
                );
                plugin.reloadConfig();
                return 1;
            }).build();
    }
}


