package me.dreamerzero.chatregulator.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import me.dreamerzero.chatregulator.RegulatorTest;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.listener.chat.ChatListener;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.utils.TestsUtils;
import me.dreamerzero.chatregulator.enums.InfractionType;

public class ChatTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(ChatTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
        RegulatorTest.set(TestsUtils.createRegulator());
    }

    @Test
    @DisplayName("Chat Listener Test")
    @Disabled("Deprecated")
    void chatListenerTest(){
        String chatMessage = "AAAA";

        Player player = TestsUtils.createNormalPlayer("JugadorRandom");

        PlayerChatEvent chatEvent = new PlayerChatEvent(player, chatMessage);
        Continuation chatContinuation = new Continuation(){
            @Override
            public void resume() {}
            @Override
            public void resumeWithException(Throwable arg0) {}
        };

        TestsUtils.createProxy().getEventManager().fire(chatEvent).thenAccept(event -> {
            Statistics stats = Statistics.getStatistics();
            final int capsCount = stats.getViolationCount(InfractionType.CAPS);
            new ChatListener().onChat(event, chatContinuation);

            final int newcapsCount = stats.getViolationCount(InfractionType.CAPS);

            fail((capsCount ) +" and " + newcapsCount+" Evento bloqueado: "+ event.getResult());
            assertEquals(capsCount + 1, newcapsCount);
        }).join();
    }
}
