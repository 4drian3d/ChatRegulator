package io.github._4drian3d.chatregulator.plugin.listener.chat;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.modules.Replacer;
import io.github._4drian3d.chatregulator.plugin.wrapper.ChatWrapper;
import io.github._4drian3d.chatregulator.plugin.wrapper.EventWrapper;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ChatRegulator's Chat Listener
 */
@Internal
public final class ChatListener {
    @Inject
    private ChatRegulator plugin;

    /**
     * Chat Listener for detections
     *
     * @param event        the chat event
     * @param continuation the event cycle
     */
    @Subscribe(order = PostOrder.FIRST)
    public void onChat(final PlayerChatEvent event, final Continuation continuation) {
        if (!event.getResult().isAllowed()) {
            continuation.resume();
            return;
        }
        final Player player = event.getPlayer();
        final AtomicReference<String> message = new AtomicReference<>(event.getMessage());
        final InfractionPlayerImpl infractor = plugin.getPlayerManager().getPlayer(player);
        final EventWrapper<PlayerChatEvent> wrapper = new ChatWrapper(event, continuation);

        if (infractor.unicode(message, wrapper)
                || infractor.caps(message, wrapper)
                || infractor.flood(message, wrapper)
                || infractor.regular(message, wrapper)
                || infractor.spam(message, wrapper)
        ) {
            return;
        }

        if (plugin.getConfig().getFormatConfig().enabled()) {
            message.set(Replacer.applyFormat(message.get(), plugin.getConfig()));
            event.setResult(ChatResult.message(message.get()));
        }

        infractor.lastMessage(message.get());
        continuation.resume();
    }
}
