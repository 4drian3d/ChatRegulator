package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.api.enums.Permission;
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

    @Override
    public CommandNode<CommandSource> node() {
        return LiteralArgumentBuilder
                .<CommandSource>literal("reload")
                .requires(Permission.COMMAND_RELOAD)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            formatter.parse(
                                    messagesContainer.get().getGeneralMessages().getReloadMessage()
                            )
                    );
                    messagesContainer.reload();
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
