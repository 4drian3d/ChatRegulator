package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.spotify.futures.CompletableFutures;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.common.configuration.*;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.Formatter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

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
    private ComponentLogger logger;

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
