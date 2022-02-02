package me.dreamerzero.chatregulator.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.listener.chat.ChatListener;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;

public class ChatTest {
    @Test
    @DisplayName("Chat Listener Test")
    void chatListenerTest(){
        EventManager eManager = mock(EventManager.class);

        String chatMessage = "Holaaaaaaaaaaaaaaaaa";
        final int floodCount = Statistics.getStatistics().getViolationCount(InfractionType.FLOOD);

        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("JugadorRandom");
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.hasPermission(Permissions.BYPASS_INFRACTIONS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_FLOOD)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_CAPS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_SPAM)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_UNICODE)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_COMMANDSPY)).thenReturn(false);

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
