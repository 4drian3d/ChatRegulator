package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.config.Messages;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Optional;

import static io.github._4drian3d.chatregulator.plugin.commands.RegulatorCommand.*;

public class PlayerArgument implements Argument {
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private IFormatter formatter;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private PlayerManagerImpl playerManager;
    @Override
    public CommandNode<CommandSource> node() {
        return literal("player")
                .requires(Permission.COMMAND_PLAYER)
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
                            playerManager.getPlayers()
                                    .stream()
                                    .limit(configurationContainer.get().getGeneralConfig().tabCompleteLimit())
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
                        .executes(cmd -> {
                            String arg = cmd.getArgument("player", String.class);
                            CommandSource source = cmd.getSource();

                            proxyServer.getPlayer(arg).ifPresentOrElse(player -> {
                                final InfractionPlayerImpl infractionPlayer = playerManager.getPlayer(player);
                                TagResolver placeholders = infractionPlayer.getPlaceholders();
                                sendLines(source, messagesContainer.get().getGeneralMessages().getPlayerFormat(), placeholders, formatter);
                            }, () -> {
                                Optional<InfractionPlayerImpl> opt = playerManager.getPlayers()
                                        .stream()
                                        .filter(player -> player.username().equals(arg))
                                        .findAny();
                                if (opt.isPresent()) {
                                    TagResolver placeholders = opt.get().getPlaceholders();
                                    sendLines(source, messagesContainer.get().getGeneralMessages().getPlayerFormat(), placeholders, formatter);
                                } else {
                                    source.sendMessage(formatter.parse(
                                            messagesContainer.get().getGeneralMessages().playerNotFound(),
                                            Placeholder.unparsed("player", arg)
                                    ));
                                }
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
