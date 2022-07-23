package me.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.modules.Replacer;
import me.dreamerzero.chatregulator.wrapper.event.ChatWrapper;
import me.dreamerzero.chatregulator.wrapper.event.EventWrapper;

import static me.dreamerzero.chatregulator.utils.GeneralUtils.*;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ChatRegulator's Chat Listener
 */
@Internal
public final class ChatListener {
    private final ChatRegulator plugin;
    public ChatListener(ChatRegulator plugin){
        this.plugin = plugin;
    }
    /**
     * Chat Listener for detections
     * @param event the chat event
     * @param continuation the event cycle
     */
    @Subscribe(order = PostOrder.FIRST)
    public void onChat(final PlayerChatEvent event, final Continuation continuation) {
        if(!event.getResult().isAllowed()){
            continuation.resume();
            return;
        }
        final Player player = event.getPlayer();
        final AtomicReference<String> message = new AtomicReference<>(event.getMessage());
        final InfractionPlayer infractor = InfractionPlayer.get(player);
        final EventWrapper<PlayerChatEvent> wrapper = new ChatWrapper(event, continuation);

        if(unicode(infractor, message, wrapper, plugin)
            || caps(infractor, message, wrapper, plugin)
            || flood(infractor, message, wrapper, plugin)
            || regular(infractor, message, wrapper, plugin)
            || spam(infractor, message, wrapper, plugin)
        ) {
            return;
        }

        if(plugin.getConfig().getFormatConfig().enabled()){
            message.set(Replacer.applyFormat(message.get()));
            event.setResult(ChatResult.message(message.get()));
        }

        infractor.lastMessage(message.get());
        continuation.resume();
    }
}
