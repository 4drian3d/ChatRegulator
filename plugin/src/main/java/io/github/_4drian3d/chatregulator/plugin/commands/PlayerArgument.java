package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class PlayerArgument implements Argument {
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private Formatter formatter;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private PlayerManagerImpl playerManager;

    @Override
    public CommandNode<CommandSource> node() {
        return literal("player")
                .requires(Permission.COMMAND_PLAYER::test)
                .executes(ctx -> {
                    ctx.getSource().sendMessage(
                            formatter.parse(
                                    messagesContainer.get().getGeneralMessages().noArgument(),
                                    ctx.getSource()
                            )
                    );
                    return Command.SINGLE_SUCCESS;
                })
                .then(argument("player", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            playerManager.getPlayers()
                                    .stream()
                                    .limit(configurationContainer.get().tabCompleteLimit())
                                    .forEach((player) -> builder.suggest(
                                            player.username(),
                                            VelocityBrigadierMessage.tooltip(
                                                    formatter.parse(
                                                            messagesContainer.get().getGeneralMessages().getPlayerSuggestionsFormat(),
                                                            player,
                                                            Placeholder.unparsed("player", player.username())
                                                    )
                                            )));
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            final String arg = ctx.getArgument("player", String.class);
                            final CommandSource source = ctx.getSource();
                            final Messages.General generalMessages = messagesContainer.get().getGeneralMessages();

                            proxyServer.getPlayer(arg).ifPresentOrElse(player -> {
                                final InfractionPlayerImpl infractionPlayer = playerManager.getPlayer(player);
                                TagResolver placeholders = infractionPlayer.getPlaceholders();
                                sendLines(source, generalMessages.getPlayerFormat(), placeholders, formatter);
                            }, () -> playerManager.getPlayers()
                                    .stream()
                                    .filter(player -> player.username().equals(arg))
                                    .findAny()
                                    .ifPresentOrElse(
                                            player -> {
                                                TagResolver placeholders = player.getPlaceholders();
                                                sendLines(source, generalMessages.getPlayerFormat(), placeholders, formatter);
                                            },
                                            () -> source.sendMessage(formatter.parse(
                                                    generalMessages.playerNotFound(),
                                                    Placeholder.unparsed("player", arg)
                                            ))
                                    ));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
