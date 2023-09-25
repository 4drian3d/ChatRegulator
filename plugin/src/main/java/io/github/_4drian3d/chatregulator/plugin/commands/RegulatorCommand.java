package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;

public final class RegulatorCommand {
    @Inject
    private ChatRegulator plugin;
    @Inject
    private CommandManager commandManager;
    @Inject
    private Injector injector;
    @Inject
    private Formatter formatter;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;

    public void register() {
        final LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder
                .<CommandSource>literal("chatregulator")
                .requires(Permission.COMMAND::test)
                .executes(cmd -> {
                    cmd.getSource().sendMessage(
                            formatter.parse(
                                    messagesContainer.get().getGeneralMessages().getInfoMessage()));
                    return Command.SINGLE_SUCCESS;
                })
                .then(injector.getInstance(StatsArgument.class).node())
                .then(injector.getInstance(PlayerArgument.class).node())
                .then(injector.getInstance(ResetArgument.class).node())
                .then(injector.getInstance(ClearArgument.class).node())
                .then(injector.getInstance(ReloadArgument.class).node())
                .build();

        final BrigadierCommand command = new BrigadierCommand(node);
        final CommandMeta meta = commandManager
                .metaBuilder(command)
                .aliases("chatr", "cregulator")
                .plugin(plugin)
                .build();
        commandManager.register(meta, command);
    }
}


