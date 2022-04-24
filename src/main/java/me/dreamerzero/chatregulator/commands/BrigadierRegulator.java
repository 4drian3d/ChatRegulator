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
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.Components;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class BrigadierRegulator {
    private BrigadierRegulator(){}

    public static void registerCommand(ChatRegulator plugin){
        LiteralCommandNode<CommandSource> commandNode = LiteralArgumentBuilder
            .<CommandSource>literal("chatregulator")
            .requires(src -> src.hasPermission(Permissions.COMMAND))
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    plugin.getFormatter().parse(
                        Configuration.getMessages().getGeneralMessages().getInfoMessage(),
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
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("clear")
                .executes(cmd -> {
                    plugin.getProxy().sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getGlobalMessage()));
                    return 1;
                }).build())
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
            .requires(src -> src.hasPermission(Permissions.COMMAND_HELP))
            .executes(cmd -> {
                sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getMainHelp(), chatrCommand, plugin.getFormatter());
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("clear")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getClearHelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                }).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("reset")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getResethelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                })
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getPlayerHelp(), chatrCommand, plugin.getFormatter());
                    return 1;
                })
            ).build();
    }

    private static LiteralCommandNode<CommandSource> statsCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_STATS))
            .executes(cmd -> {
                TagResolver resolver = cmd.getSource() instanceof Player player
                    ? PlaceholderUtils.getPlaceholders(InfractionPlayer.get(player))
                    : PlaceholderUtils.getGlobalPlaceholders();
                sendLines(
                    cmd.getSource(),
                    Configuration.getMessages().getGeneralMessages().getStatsFormat(),
                    resolver,
                    plugin.getFormatter());
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> playerCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_PLAYER))
            .executes(cmd -> {
                cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().noArgument(), cmd.getSource()));
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    CommandSource source = cmd.getSource();
                    Optional<Player> optionalPlayer = plugin.getProxy().getPlayer(arg);
                    if(optionalPlayer.isPresent()){
                        InfractionPlayer infractionPlayer = InfractionPlayer.get(optionalPlayer.get());
                        TagResolver placeholders = PlaceholderUtils.getPlaceholders(infractionPlayer);
                        sendLines(source, Configuration.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                    } else {
                        Optional<InfractionPlayer> opt = plugin.getChatPlayers().values().stream().filter(p -> p.username().equals(arg)).findAny();
                        if(opt.isPresent()){
                            TagResolver placeholders = PlaceholderUtils.getPlaceholders(opt.get());
                            sendLines(source, Configuration.getMessages().getGeneralMessages().getPlayerFormat(), placeholders, plugin.getFormatter());
                        } else {
                            source.sendMessage(plugin.getFormatter().parse(
                                Configuration.getMessages().getGeneralMessages().playerNotFound(),
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
            .requires(src -> src.hasPermission(Permissions.COMMAND_RESET))
            .executes(cmd -> {
                cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().noArgument(), cmd.getSource()));
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    plugin.getChatPlayers().forEach((uuid, p) -> builder.suggest(p.username(), VelocityBrigadierMessage.tooltip(
                        plugin.getFormatter().parse(
                            Configuration.getMessages().getGeneralMessages().getPlayerSuggestionsFormat(),
                            p,
                            Placeholder.unparsed("player", p.username())
                        )
                    )));
                    return builder.buildFuture();
                })
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    InfractionPlayer p = InfractionPlayer.get(arg);
                    if(p == null){
                        cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().playerNotFound(), cmd.getSource(), Placeholder.unparsed("player", arg)));
                        return 1;
                    }
                    resetAll(p, cmd.getSource(), plugin.getFormatter());
                    return 1;
                })
                .then(resetWithPlayerSubcommand("infractions", InfractionType.REGULAR, Permissions.COMMAND_RESET_REGULAR, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("regular", InfractionType.REGULAR, Permissions.COMMAND_RESET_REGULAR, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("flood", InfractionType.FLOOD, Permissions.COMMAND_RESET_FLOOD, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("spam", InfractionType.SPAM, Permissions.COMMAND_RESET_SPAM, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("command", InfractionType.BCOMMAND, Permissions.COMMAND_RESET_BCOMMAND, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("unicode", InfractionType.UNICODE, Permissions.COMMAND_RESET_UNICODE, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("caps", InfractionType.CAPS, Permissions.COMMAND_RESET_CAPS, plugin.getFormatter()))
                .then(resetWithPlayerSubcommand("syntax", InfractionType.SYNTAX, Permissions.COMMAND_RESET_SYNTAX, plugin.getFormatter()))
                )
            .build();
    }

    private static void resetAll(InfractionPlayer player, Audience source, IFormatter formatter){
        player.getViolations().resetViolations(
            InfractionType.SPAM,
            InfractionType.FLOOD,
            InfractionType.REGULAR,
            InfractionType.BCOMMAND,
            InfractionType.UNICODE,
            InfractionType.CAPS,
            InfractionType.SYNTAX
        );
        ConfigManager.sendResetMessage(source, InfractionType.NONE, player, formatter);
    }

    private  static LiteralCommandNode<CommandSource> resetWithPlayerSubcommand(String subcommand, InfractionType type, String resetPermission, IFormatter formatter){
        return LiteralArgumentBuilder
            .<CommandSource>literal(subcommand)
            .requires(p -> p.hasPermission(resetPermission))
            .executes(cmd -> {
                String arg = cmd.getArgument("player", String.class);
                InfractionPlayer p = InfractionPlayer.get(arg);
                if(p == null){
                    cmd.getSource().sendMessage(formatter.parse(Configuration.getMessages().getGeneralMessages().playerNotFound(), cmd.getSource(), Placeholder.unparsed("player", arg)));
                    return 1;
                }
                p.getViolations().resetViolations(type);
                ConfigManager.sendResetMessage(cmd.getSource(), type, p, formatter);
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> clearCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR))
            .executes(cmd -> {
                plugin.getProxy().sendMessage(Components.SPACES_COMPONENT);
                cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getGlobalMessage()));
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("server")
                .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR_SERVER))
                .executes(cmd -> {
                    if (cmd.getSource() instanceof final Player player) {
                        player.getCurrentServer().ifPresent(playerServer -> {
                            playerServer.getServer().sendMessage(Components.SPACES_COMPONENT);
                            player.sendMessage(plugin.getFormatter().parse(
                                Configuration.getMessages().getClearMessages().getServerMessage(),
                                player,
                                Placeholder.unparsed("server", playerServer.getServerInfo().getName())
                            ));
                        });
                    } else {
                        cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().noArgument(), cmd.getSource()));
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
                            cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getServerMessage(), cmd.getSource(), serverPlaceholder));
                        }, () -> cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getNotFoundServerMessage(), cmd.getSource(), serverPlaceholder)));
                        return 1;
                    }).build()
                )
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR_PLAYER))
                .executes(cmd -> {
                    cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().noArgument(), cmd.getSource()));
                    return 1;
                })
                .then(RequiredArgumentBuilder
                    .<CommandSource, String>argument("player", StringArgumentType.word())
                    .suggests((arg, builder) -> {
                        plugin.getProxy().getAllPlayers().forEach(p -> builder.suggest(p.getUsername()));
                        return builder.buildFuture();
                    })
                    .executes(cmd -> {
                        String arg = cmd.getArgument("player", String.class);
                        TagResolver serverPlaceholder = Placeholder.unparsed("player", arg);
                        plugin.getProxy().getPlayer(arg).ifPresentOrElse(playerObjetive -> {
                            playerObjetive.sendMessage(Components.SPACES_COMPONENT);
                            cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getPlayerMessage(), cmd.getSource(), serverPlaceholder));
                        }, () -> cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getNotFoundServerMessage(), cmd.getSource(), serverPlaceholder)));
                        return 1;
                    }).build()
                ).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("all")
                .executes(cmd -> {
                    plugin.getProxy().sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getClearMessages().getGlobalMessage(), cmd.getSource()));
                    return 1;
                }).build()
            ).build();

    }

    private static LiteralCommandNode<CommandSource> reloadCommand(String command, ChatRegulator plugin){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(p -> p.hasPermission(Permissions.COMMAND_RELOAD))
            .executes(cmd -> {
                cmd.getSource().sendMessage(plugin.getFormatter().parse(Configuration.getMessages().getGeneralMessages().getReloadMessage()));
                plugin.reloadConfig();
                return 1;
            }).build();
    }
}


