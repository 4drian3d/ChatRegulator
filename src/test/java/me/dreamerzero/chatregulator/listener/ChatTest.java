package me.dreamerzero.chatregulator.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.listener.chat.ChatListener;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.utils.TestsUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;

public class ChatTest {
    @Test
    @DisplayName("Chat Listener Test")
    void chatListenerTest(){
        EventManager eManager = mock(EventManager.class);

        String chatMessage = "Holaaaaaaaaaaaaaaaaa";
        final int floodCount = Statistics.getStatistics().getViolationCount(InfractionType.FLOOD);

        Player player = TestsUtils.createNormalPlayer("JugadorRandom");

        PlayerChatEvent chatEvent = new PlayerChatEvent(player, chatMessage);
        Continuation chatContinuation = mock(Continuation.class);

        when(eManager.fire(chatEvent)).thenReturn(CompletableFuture.completedFuture(chatEvent));

        eManager.fire(chatEvent).thenAccept(event -> {
            ChatListener chatListener = new ChatListener();
            chatListener.onChat(event, chatContinuation);
            final int newFloodCount = Statistics.getStatistics().getViolationCount(InfractionType.FLOOD);
            assertEquals(floodCount + 1, newFloodCount);
        });
    }
}
