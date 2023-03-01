package io.github._4drian3d.chatregulator.plugin.listener.command;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.utils.Commands;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.wrapper.CommandWrapper;
import io.github._4drian3d.chatregulator.plugin.wrapper.EventWrapper;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Detections related to command execution by players
 */
@Internal
public final class CommandListener {
    @Inject
    private ChatRegulator plugin;

    /**
     * Listener for command detections
     *
     * @param event        the command event
     * @param continuation the event cycle
     */
    @Subscribe(order = PostOrder.FIRST)
    public void onCommand(CommandExecuteEvent event, Continuation continuation) {
        if (!(event.getCommandSource() instanceof final Player player)
                || !event.getResult().isAllowed()
        ) {
            continuation.resume();
            return;
        }

        final InfractionPlayerImpl infractionPlayer = plugin.getPlayerManager().getPlayer(player);
        final EventWrapper<CommandExecuteEvent> wrapper = new CommandWrapper(event, continuation);

        if (infractionPlayer.blockedCommands(event.getCommand(), wrapper)
                || infractionPlayer.syntax(event.getCommand(), wrapper)
                || this.checkIfCanCheck(event.getCommand(), wrapper)
        ) {
            return;
        }

        final AtomicReference<String> command = new AtomicReference<>(event.getCommand());

        if (infractionPlayer.unicode(command, wrapper)
                || infractionPlayer.caps(command, wrapper)
                || infractionPlayer.flood(command, wrapper)
                || infractionPlayer.regular(command, wrapper)
                || infractionPlayer.spam(command, wrapper)
        ) {
            return;
        }

        infractionPlayer.lastCommand(command.get());
        continuation.resume();
    }

    private boolean checkIfCanCheck(final String command, EventWrapper<?> wrapper) {
        for (final String cmd : plugin.getConfig().getCommandsChecked()) {
            if (Commands.isStartingString(command, cmd))
                return false;
        }
        wrapper.resume();
        return true;
    }
}
