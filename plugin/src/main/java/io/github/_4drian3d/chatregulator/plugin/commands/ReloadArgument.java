package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.spotify.futures.CompletableFutures;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.common.configuration.Blacklist;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.config.*;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import io.github._4drian3d.velocityhexlogger.HexLogger;

public class ReloadArgument implements Argument {
    @Inject
    private Formatter formatter;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private ConfigurationContainer<Checks> checksContainer;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private ConfigurationContainer<Blacklist> blacklistContainer;
    @Inject
    private HexLogger logger;

    @Override
    public CommandNode<CommandSource> node() {
        return LiteralArgumentBuilder
                .<CommandSource>literal("reload")
                .requires(Permission.COMMAND_RELOAD::test)
                .executes(cmd -> {
                    CompletableFutures.combine(
                            messagesContainer.reload(),
                            configurationContainer.reload(),
                            checksContainer.reload(),
                            blacklistContainer.reload(),
                            (a, b, c, d) -> a && b && c && d
                    ).thenAccept(
                        result -> {
                            final var message = formatter.parse(messagesContainer.get().getGeneralMessages().getReloadMessage());
                            if (cmd.getSource() instanceof ConsoleCommandSource) {
                                logger.info(message);
                            } else {
                                cmd.getSource().sendMessage(message);
                            }
                        }
                    );
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
