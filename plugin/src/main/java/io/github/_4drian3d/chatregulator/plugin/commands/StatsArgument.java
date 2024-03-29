package io.github._4drian3d.chatregulator.plugin.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.common.impl.StatisticsImpl;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class StatsArgument implements Argument {
    @Inject
    private PlayerManagerImpl playerManager;
    @Inject
    private StatisticsImpl statistics;
    @Inject
    private ConfigurationContainer<Messages> messagesContainer;
    @Inject
    private Formatter formatter;

    @Override
    public CommandNode<CommandSource> node() {
        return literal("stats")
                .requires(Permission.COMMAND_STATS::test)
                .executes(cmd -> {
                    final TagResolver resolver;
                    if (cmd.getSource() instanceof Player player) {
                        InfractionPlayerImpl infractionPlayer = playerManager.getPlayer(player);
                        resolver = infractionPlayer.getPlaceholders();
                    } else {
                        resolver = statistics;
                    }
                    sendLines(
                            cmd.getSource(),
                            messagesContainer.get().getGeneralMessages().getStatsFormat(),
                            resolver,
                            formatter
                    );
                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
