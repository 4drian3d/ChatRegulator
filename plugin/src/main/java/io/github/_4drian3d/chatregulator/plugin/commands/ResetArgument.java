package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class ResetArgument implements Argument {
    @Inject
    private Formatter formatter;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private PlayerManagerImpl playerManager;
    @Inject
    private ProxyServer proxyServer;

    @Override
    public CommandNode<CommandSource> node() {
        return literal("reset")
                .requires(Permission.COMMAND_RESET::test)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            formatter.parse(
                                    messagesContainer.get().getGeneralMessages().noArgument(),
                                    cmd.getSource()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(argument("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            playerManager.getPlayers().forEach((p) -> builder.suggest(p.username()));
                            return builder.buildFuture();
                        })
                        .executes(cmd -> {
                            final String arg = cmd.getArgument("player", String.class);
                            final Player player = proxyServer.getPlayer(arg).orElse(null);
                            if (player == null) {
                                cmd.getSource().sendMessage(
                                        formatter.parse(
                                                messagesContainer.get().getGeneralMessages().playerNotFound(),
                                                cmd.getSource(), Placeholder.unparsed("player", arg)
                                        )
                                );
                                return Command.SINGLE_SUCCESS;
                            }
                            final InfractionPlayerImpl p = playerManager.getPlayer(player);

                            p.getInfractions().resetViolations(InfractionType.GLOBAL);
                            p.sendResetMessage(cmd.getSource(), InfractionType.GLOBAL);
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(subReset("regex", InfractionType.REGEX, Permission.COMMAND_RESET_REGEX))
                        .then(subReset("regular", InfractionType.REGEX, Permission.COMMAND_RESET_REGEX))
                        .then(subReset("flood", InfractionType.FLOOD, Permission.COMMAND_RESET_FLOOD))
                        .then(subReset("spam", InfractionType.SPAM, Permission.COMMAND_RESET_SPAM))
                        .then(subReset("command", InfractionType.BLOCKED_COMMAND, Permission.COMMAND_RESET_BLOCKEDCOMMAND))
                        .then(subReset("unicode", InfractionType.UNICODE, Permission.COMMAND_RESET_UNICODE))
                        .then(subReset("caps", InfractionType.CAPS, Permission.COMMAND_RESET_CAPS))
                        .then(subReset("syntax", InfractionType.SYNTAX, Permission.COMMAND_RESET_SYNTAX))
                )
                .build();
    }

    private LiteralCommandNode<CommandSource> subReset(String subcommand, InfractionType type, Permission resetPermission) {
        return literal(subcommand)
                .requires(resetPermission::test)
                .executes(cmd -> {
                    String arg = cmd.getArgument("player", String.class);
                    Player player = proxyServer.getPlayer(arg).orElse(null);
                    if (player == null) {
                        cmd.getSource().sendMessage(
                                formatter.parse(
                                        messagesContainer.get().getGeneralMessages().playerNotFound(),
                                        cmd.getSource(),
                                        Placeholder.unparsed("player", arg)
                                )
                        );
                        return Command.SINGLE_SUCCESS;
                    }
                    InfractionPlayerImpl p = playerManager.getPlayer(player);
                    p.getInfractions().resetViolations(type);
                    p.sendResetMessage(cmd.getSource(), type);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
