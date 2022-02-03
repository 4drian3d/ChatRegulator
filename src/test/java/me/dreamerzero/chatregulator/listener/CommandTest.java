package me.dreamerzero.chatregulator.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.listener.command.CommandListener;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.utils.TestsUtils;

public class CommandTest {
    @Test
    @DisplayName("Command Listener Test")
    void commandListenerTest(){
        EventManager eManager = mock(EventManager.class);

        Player player = TestsUtils.createNormalPlayer("JugadorRandom");

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
