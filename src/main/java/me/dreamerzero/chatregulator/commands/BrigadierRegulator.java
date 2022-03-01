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
import me.dreamerzero.chatregulator.utils.PlaceholderUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class BrigadierRegulator {
    private BrigadierRegulator(){}

    public static void registerCommand(Collection<InfractionPlayer> players){
        LiteralCommandNode<CommandSource> commandNode = LiteralArgumentBuilder
            .<CommandSource>literal("chatregulator")
            .requires(src -> src.hasPermission(Permissions.COMMAND))
            .executes(cmd -> {
                cmd.getSource().sendMessage(
                    MiniMessage.miniMessage().deserialize(
                        Configuration.getMessages().getGeneralMessages().getInfoMessage(),
                        Placeholder.unparsed("command", cmd.getInput())));
                return 1;
            })
            .then(infoSubCommand("help"))
            .then(infoSubCommand("info"))
            .then(statsCommand("stats"))
            .then(playerCommand("player", players))
            .then(resetCommand("reset", players))
            .then(clearCommand("clear"))
            .then(reloadCommand("reload"))
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("clear")
                .executes(cmd -> {
                    ChatRegulator.getInstance().getProxy().sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getGlobalMessage()));
                    return 1;
                }).build())
            .build();

        ProxyServer proxy = ChatRegulator.getInstance().getProxy();
        BrigadierCommand bCommand = new BrigadierCommand(commandNode);
        CommandMeta meta = proxy.getCommandManager().metaBuilder(bCommand).aliases("chatr", "cregulator").build();
        proxy.getCommandManager().register(meta, bCommand);
    }

    private static void sendLines(Audience sender, Collection<String> lines){
        lines.forEach(ln -> sender.sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(ln)));
    }

    private static void sendLines(Audience sender, Collection<String> lines, TagResolver resolver){
        lines.forEach(ln -> sender.sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(ln, resolver)));
    }

    private static final TagResolver chatrCommand = Placeholder.unparsed("command", "chatregulator");

    private static LiteralCommandNode<CommandSource> infoSubCommand(String command){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_HELP))
            .executes(cmd -> {
                sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getMainHelp(), chatrCommand);
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("clear")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getClearHelp(), chatrCommand);
                    return 1;
                }).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("reset")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getResethelp(), chatrCommand);
                    return 1;
                })
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .executes(cmd -> {
                    sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getHelpMessages().getPlayerHelp(), chatrCommand);
                    return 1;
                })
            ).build();
    }

    private static LiteralCommandNode<CommandSource> statsCommand(String command){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_STATS))
            .executes(cmd -> {
                sendLines(cmd.getSource(), Configuration.getMessages().getGeneralMessages().getStatsFormat());
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> playerCommand(String command, Collection<InfractionPlayer> players){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_PLAYER))
            .executes(cmd -> {
                cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().noArgument()));
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    CommandSource source = cmd.getSource();
                    Optional<Player> optionalPlayer = ChatRegulator.getInstance().getProxy().getPlayer(arg);
                    if(optionalPlayer.isPresent()){
                        InfractionPlayer infractionPlayer = InfractionPlayer.get(optionalPlayer.get());
                        TagResolver placeholders = PlaceholderUtils.getPlaceholders(infractionPlayer);
                        sendLines(source, Configuration.getMessages().getGeneralMessages().getPlayerFormat(), placeholders);
                    } else {
                        Optional<InfractionPlayer> opt = players.stream().filter(p -> p.username().equals(arg)).findAny();
                        if(opt.isPresent()){
                            TagResolver placeholders = PlaceholderUtils.getPlaceholders(opt.get());
                            sendLines(source, Configuration.getMessages().getGeneralMessages().getPlayerFormat(), placeholders);
                        } else {
                            source.sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(
                                Configuration.getMessages().getGeneralMessages().playerNotFound(),
                                Placeholder.unparsed("player", arg)
                            ));
                        }
                    }
                    return 1;
                }))
            .build();
    }

    private static LiteralCommandNode<CommandSource> resetCommand(String command, Collection<InfractionPlayer> players){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(src -> src.hasPermission(Permissions.COMMAND_RESET))
            .executes(cmd -> {
                cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().noArgument()));
                return 1;
            })
            .then(RequiredArgumentBuilder
                .<CommandSource, String>argument("player", StringArgumentType.word())
                //TODO: Configurable message
                .suggests((argument, builder) -> {
                    players.forEach(p -> builder.suggest(p.username(), VelocityBrigadierMessage.tooltip(
                        Component.text("Reset ", NamedTextColor.AQUA)
                        .append(Component.text(p.username(), NamedTextColor.WHITE))
                        .append(Component.text(" infractions"))
                    )));
                    return builder.buildFuture();
                })
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    InfractionPlayer p = InfractionPlayer.get(arg);
                    if(p == null){
                        cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().playerNotFound(), Placeholder.unparsed("player", arg)));
                        return 1;
                    }
                    resetAll(p, cmd.getSource());
                    return 1;
                })
                .then(resetWithPlayerSubcommand("infractions", InfractionType.REGULAR, Permissions.COMMAND_RESET_REGULAR))
                .then(resetWithPlayerSubcommand("regular", InfractionType.REGULAR, Permissions.COMMAND_RESET_REGULAR))
                .then(resetWithPlayerSubcommand("flood", InfractionType.FLOOD, Permissions.COMMAND_RESET_FLOOD))
                .then(resetWithPlayerSubcommand("spam", InfractionType.SPAM, Permissions.COMMAND_RESET_SPAM))
                .then(resetWithPlayerSubcommand("command", InfractionType.BCOMMAND, Permissions.COMMAND_RESET_BCOMMAND))
                .then(resetWithPlayerSubcommand("unicode", InfractionType.UNICODE, Permissions.COMMAND_RESET_UNICODE))
                .then(resetWithPlayerSubcommand("caps", InfractionType.CAPS, Permissions.COMMAND_RESET_CAPS))
                .then(resetWithPlayerSubcommand("syntax", InfractionType.SYNTAX, Permissions.COMMAND_RESET_SYNTAX))
                )
            .build();
    }

    private static void resetAll(InfractionPlayer player, Audience source){
        player.getViolations().resetViolations(
            InfractionType.SPAM,
            InfractionType.FLOOD,
            InfractionType.REGULAR,
            InfractionType.BCOMMAND,
            InfractionType.UNICODE,
            InfractionType.CAPS,
            InfractionType.SYNTAX
        );
        ConfigManager.sendResetMessage(source, InfractionType.NONE, player);
    }

    private  static LiteralCommandNode<CommandSource> resetWithPlayerSubcommand(String subcommand, InfractionType type, String resetPermission){
        return LiteralArgumentBuilder
            .<CommandSource>literal(subcommand)
            .requires(p -> p.hasPermission(resetPermission))
            .executes(cmd -> {
                String arg = cmd.getArgument("player", String.class);
                InfractionPlayer p = InfractionPlayer.get(arg);
                if(p == null){
                    cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().playerNotFound(), Placeholder.unparsed("player", arg)));
                    return 1;
                }
                p.getViolations().resetViolations(type);
                ConfigManager.sendResetMessage(cmd.getSource(), type, p);
                return 1;
            }).build();
    }

    private static LiteralCommandNode<CommandSource> clearCommand(String command){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR))
            .executes(cmd -> {
                ChatRegulator.getInstance().getProxy().sendMessage(Components.SPACES_COMPONENT);
                cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getGlobalMessage()));
                return 1;
            })
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("server")
                .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR_SERVER))
                .executes(cmd -> {
                    if (cmd.getSource() instanceof Player) {
                        Player player = (Player)cmd.getSource();
                        player.getCurrentServer().ifPresent(playerServer -> {
                            playerServer.getServer().sendMessage(Components.SPACES_COMPONENT);
                            player.sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(
                                Configuration.getMessages().getClearMessages().getServerMessage(),
                                Placeholder.unparsed("server", playerServer.getServerInfo().getName())
                            ));
                        });
                    } else {
                        cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().noArgument()));
                    }
                    return 1;
                })
                .then(RequiredArgumentBuilder
                    .<CommandSource, String>argument("server", StringArgumentType.word())
                    .suggests((arg, builder) -> {
                        ChatRegulator.getInstance().getProxy().getAllServers().forEach(sv -> builder.suggest(sv.getServerInfo().getName()));
                        return builder.buildFuture();
                    })
                    .executes(cmd -> {
                        String arg = cmd.getArgument("server", String.class);
                        TagResolver serverPlaceholder = Placeholder.unparsed("server", arg);
                        ChatRegulator.getInstance().getProxy().getServer(arg).ifPresentOrElse(serverObjetive -> {
                            serverObjetive.sendMessage(Components.SPACES_COMPONENT);
                            cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getServerMessage(), serverPlaceholder));
                        }, () -> cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getNotFoundServerMessage(), serverPlaceholder)));
                        return 1;
                    }).build()
                )
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("player")
                .requires(p -> p.hasPermission(Permissions.COMMAND_CLEAR_PLAYER))
                .executes(cmd -> {
                    cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().noArgument()));
                    return 1;
                })
                .then(RequiredArgumentBuilder
                    .<CommandSource, String>argument("player", StringArgumentType.word())
                    .suggests((arg, builder) -> {
                        ChatRegulator.getInstance().getProxy().getAllPlayers().forEach(p -> builder.suggest(p.getUsername()));
                        return builder.buildFuture();
                    })
                    .executes(cmd -> {
                        String arg = cmd.getArgument("player", String.class);
                        TagResolver serverPlaceholder = Placeholder.unparsed("player", arg);
                        ChatRegulator.getInstance().getProxy().getPlayer(arg).ifPresentOrElse(playerObjetive -> {
                            playerObjetive.sendMessage(Components.SPACES_COMPONENT);
                            cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getPlayerMessage(), serverPlaceholder));
                        }, () -> cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getNotFoundServerMessage(), serverPlaceholder)));
                        return 1;
                    }).build()
                ).build()
            )
            .then(LiteralArgumentBuilder
                .<CommandSource>literal("all")
                .executes(cmd -> {
                    ChatRegulator.getInstance().getProxy().sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getClearMessages().getGlobalMessage()));
                    return 1;
                }).build()
            ).build();

    }

    private static LiteralCommandNode<CommandSource> reloadCommand(String command){
        return LiteralArgumentBuilder
            .<CommandSource>literal(command)
            .requires(p -> p.hasPermission(Permissions.COMMAND_RELOAD))
            .executes(cmd -> {
                cmd.getSource().sendMessage(Components.MESSAGE_MINIMESSAGE.deserialize(Configuration.getMessages().getGeneralMessages().getReloadMessage()));
                ChatRegulator.getInstance().reloadConfig();
                return 1;
            }).build();
    }
}


