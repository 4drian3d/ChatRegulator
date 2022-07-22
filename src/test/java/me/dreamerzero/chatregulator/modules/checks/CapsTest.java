package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.TestsUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils.EventBundle;

class CapsTest {
    @BeforeAll
    static void loadConfig(@TempDir Path path){
        Configuration.loadConfig(path, LoggerFactory.getLogger(InfractionTest.class));
    }

    @Test
    @DisplayName("Caps Test")
    void capsTest(){
        String original = "HELLO EVERYONE";
        String expected = "hello everyone";

        CapsCheck.builder().limit(5).build().check(original).thenAccept(result -> {
            assertTrue(result.isInfraction());
            assertTrue(result instanceof IReplaceable);
            String replaced = ((IReplaceable)result).replaceInfraction();

            assertEquals(expected, replaced);
        }).join();
    }

    @Test
    void realTest(@TempDir Path path){
        String message = "AAAAAAAAAA";
        Player player = TestsUtils.createRandomNormalPlayer();
        assertTrue(GeneralUtils.allowedPlayer(player, InfractionType.CAPS));
        var result = CapsCheck.createCheck(message).join();
        assertTrue(GeneralUtils.checkAndCall(
            new EventBundle(
                InfractionPlayer.get(player),
                message, InfractionType.CAPS,
                result, SourceType.CHAT
            ), TestsUtils.createRegulator(path)));
        assertTrue(result instanceof IReplaceable);
        String messageReplaced = ((IReplaceable)result).replaceInfraction();
        assertEquals("aaaaaaaaaa", messageReplaced);
    }
}
