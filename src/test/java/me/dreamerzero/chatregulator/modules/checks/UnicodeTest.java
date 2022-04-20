package me.dreamerzero.chatregulator.modules.checks;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.modules.StatisticsUtils;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.TestsUtils;

public class UnicodeTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(UnicodeTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Illegal Check")
    void illegalTest(){
        String illegal = "ƕƘaea";
        String expected = "  aea";

        assertEquals(expected, UnicodeCheck.builder().replaceable(true).build().check(illegal).thenApply(result -> {
            assertTrue(result.isInfraction());

            assertTrue(result instanceof IReplaceable);

            return ((IReplaceable)result).replaceInfraction();
        }).join());
    }

    @Test
    @DisplayName("Custom Check")
    void custom(){
        String illegal = "ñn't";

        assertTrue(UnicodeCheck.builder()
            .characters('ñ')
            .build()
            .check(illegal)
            .thenApply(Result::isInfraction)
            .join()
        );
    }

    @Test
    void legalCheck(){
        String legal = "Hello my friends AA";

        assertFalse(UnicodeCheck.createCheck(legal).join().isInfraction());
    }

    @Test
    void realTest(){
        String randomMSG = "ƕƘáéíóú";
        Player player = TestsUtils.createRandomNormalPlayer();
        assertTrue(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE));
        UnicodeCheck.builder().replaceable(true).build().check(randomMSG).thenAccept(result -> {
            assertTrue(GeneralUtils.callViolationEvent(InfractionPlayer.get(player), randomMSG, InfractionType.UNICODE, result, SourceType.CHAT, TestsUtils.createRegulator()));
            assertTrue(result instanceof IReplaceable);
            IReplaceable replaceableResult = (IReplaceable)result;
            String messageReplaced = replaceableResult.replaceInfraction();
            assertEquals("  áéíóú",messageReplaced);
            StatisticsUtils.resetStatistics();
        }).join();
    }

    @Test
    void builderTest() {
        UnicodeCheck.Builder builder = UnicodeCheck.builder()
            .characters('a');

        assertTrue(builder.blockCharacters().build()
            .check("aaaaaa")
            .thenApply(Result::isInfraction)
            .join());
        assertFalse(builder.allowCharacters().build()
            .check("aaaaaaa")
            .thenApply(Result::isInfraction)
            .join());
    }
}
