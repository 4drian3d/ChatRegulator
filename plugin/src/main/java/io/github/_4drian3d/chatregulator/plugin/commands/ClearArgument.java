package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import io.github._4drian3d.chatregulator.api.utils.Components;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class ClearArgument implements Argument {
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private Formatter formatter;
    @Inject
    private PlayerManagerImpl playerManager;
    @Override
    public CommandNode<CommandSource> node() {
        return literal("clear")
                .requires(Permission.COMMAND_CLEAR::test)
                .executes(cmd -> {
                    proxyServer.sendMessage(Components.SPACES_COMPONENT);
                    cmd.getSource().sendMessage(
                            formatter.parse(
                                    messagesContainer.get().getClearMessages().getGlobalMessage()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(literal("server")
                        .requires(Permission.COMMAND_CLEAR_SERVER::test)
                        .executes(cmd -> {
                            if (cmd.getSource() instanceof final Player player) {
                                player.getCurrentServer().ifPresent(playerServer -> {
                                    playerServer.getServer().sendMessage(Components.SPACES_COMPONENT);
                                    player.sendMessage(formatter.parse(
                                            messagesContainer.get().getClearMessages().getServerMessage(),
                                            player,
                                            Placeholder.unparsed("server", playerServer.getServerInfo().getName())
                                    ));
                                });
                            } else {
                                cmd.getSource().sendMessage(
                                        formatter.parse(
                                                messagesContainer.get().getGeneralMessages().noArgument(),
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
                                    final String arg = cmd.getArgument("server", String.class);
                                    final TagResolver serverPlaceholder = Placeholder.unparsed("server", arg);

                                    proxyServer.getServer(arg).ifPresentOrElse(serverObjective -> {
                                        serverObjective.sendMessage(Components.SPACES_COMPONENT);
                                        cmd.getSource().sendMessage(
                                                formatter.parse(
                                                        messagesContainer.get().getClearMessages().getServerMessage(),
                                                        cmd.getSource(),
                                                        serverPlaceholder
                                                )
                                        );
                                    }, () -> cmd.getSource().sendMessage(
                                            formatter.parse(
                                                    messagesContainer.get().getClearMessages().getNotFoundServerMessage(),
                                                    cmd.getSource(),
                                                    serverPlaceholder
                                            )
                                    ));
                                    return Command.SINGLE_SUCCESS;
                                }).build()
                        )
                )
                .then(literal("player")
                        .requires(Permission.COMMAND_CLEAR_PLAYER::test)
                        .executes(cmd -> {
                            cmd.getSource().sendMessage(
                                    formatter.parse(
                                            messagesContainer.get().getGeneralMessages().noArgument(),
                                            cmd.getSource()
                                    )
                            );
                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                        .then(argument("player", StringArgumentType.word())
                                .suggests((arg, builder) -> {
                                    playerManager.getPlayers().forEach(p -> builder.suggest(p.username()));
                                    return builder.buildFuture();
                                })
                                .executes(cmd -> {
                                    final String arg = cmd.getArgument("player", String.class);
                                    final TagResolver serverPlaceholder = Placeholder.unparsed("player", arg);

                                    proxyServer.getPlayer(arg).ifPresentOrElse(playerObjective -> {
                                        playerObjective.sendMessage(Components.SPACES_COMPONENT);
                                        cmd.getSource().sendMessage(
                                                formatter.parse(
                                                        messagesContainer.get().getClearMessages().getPlayerMessage(),
                                                        cmd.getSource(),
                                                        serverPlaceholder
                                                )
                                        );
                                    }, () -> cmd.getSource().sendMessage(
                                            formatter.parse(
                                                    messagesContainer.get().getClearMessages().getNotFoundServerMessage(),
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
                                    formatter.parse(
                                            messagesContainer.get().getClearMessages().getGlobalMessage(),
                                            cmd.getSource()
                                    )
                            );
                            return Command.SINGLE_SUCCESS;
                        }).build()
                ).build();
    }
}
