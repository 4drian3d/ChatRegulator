package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.spotify.futures.CompletableFutures;
import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.config.Blacklist;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.config.Messages;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;

public class ReloadArgument implements Argument {
    @Inject
    private IFormatter formatter;
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private ConfigurationContainer<Blacklist> blacklistContainer;

    @Override
    public CommandNode<CommandSource> node() {
        return LiteralArgumentBuilder
                .<CommandSource>literal("reload")
                .requires(Permission.COMMAND_RELOAD)
                .executes(cmd -> {
                    CompletableFutures.combine(
                            messagesContainer.reload(),
                            configurationContainer.reload(),
                            blacklistContainer.reload(),
                            (a, b, c) -> a && b && c
                    ).thenAccept(
                        result -> cmd.getSource().sendMessage(formatter.parse(messagesContainer.get().getGeneralMessages().getReloadMessage()))
                    );
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
