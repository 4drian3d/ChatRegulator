package me.dreamerzero.chatregulator.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.listener.command.CommandListener;
import me.dreamerzero.chatregulator.modules.Statistics;

public class CommandTest {
    @Test
    @DisplayName("Command Listener Test")
    void commandListenerTest(){
        EventManager eManager = mock(EventManager.class);

        Player player = mock(Player.class);
        when(player.getUsername()).thenReturn("JugadorRandom");
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.hasPermission(Permissions.BYPASS_INFRACTIONS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_FLOOD)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_CAPS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_SPAM)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_UNICODE)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_COMMANDSPY)).thenReturn(false);

        String command = "tell 4drian3d fuck you";
        CommandExecuteEvent commandEvent = new CommandExecuteEvent(player, command);
        Continuation commandContinuation = mock(Continuation.class);
        final int infractionInitialCount = Statistics.getStatistics().getViolationCount(InfractionType.REGULAR);
        when(eManager.fire(commandEvent)).thenReturn(CompletableFuture.completedFuture(commandEvent));

        eManager.fire(commandEvent).thenAccept(event -> {
            CommandListener commandListener = new CommandListener();
            commandListener.onCommand(event, commandContinuation);
            final int newInfractionCount = Statistics.getStatistics().getViolationCount(InfractionType.REGULAR);
            assertEquals(infractionInitialCount+1, newInfractionCount);
        });
    }
}
