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
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck.CharMode;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.TestsUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils.EventBundle;

public final class UnicodeTest {
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

        UnicodeCheck check = UnicodeCheck.builder().controlType(ControlType.REPLACE).build();
        Result result = check.check(illegal).join();

        assertTrue(result.isInfraction());

        assertTrue(result instanceof IReplaceable);

        IReplaceable replaceable = ((IReplaceable)result);

        assertEquals(expected, replaceable.replaceInfraction());
    }

    @Test
    @DisplayName("Custom Check")
    void custom(){
        String illegal = "ñn't";

        var result = UnicodeCheck.builder()
            .characters('ñ')
            .build()
            .check(illegal)
            .join();

        assertTrue(result.isInfraction());
    }

    @Test
    void legalCheck(){
        assertFalse(UnicodeCheck.createCheck("Hello my friends").join().isInfraction());
        assertFalse(UnicodeCheck.createCheck("Hola").join().isInfraction());
        assertFalse(UnicodeCheck.createCheck("aeiou nosequemasponer").join().isInfraction());
    }

    @Test
    void realTest(){
        String randomMSG = "ƕƘáéíóú";
        Player player = TestsUtils.createRandomNormalPlayer();

        assertTrue(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE));

        UnicodeCheck check = UnicodeCheck.builder().controlType(ControlType.REPLACE).build();
        Result result = check.check(randomMSG).join();

        assertTrue(GeneralUtils.callViolationEvent(new EventBundle(InfractionPlayer.get(player), randomMSG, InfractionType.UNICODE, result, SourceType.CHAT), TestsUtils.createRegulator()));
        assertTrue(result instanceof IReplaceable);
        IReplaceable replaceableResult = (IReplaceable)result;

        String messageReplaced = replaceableResult.replaceInfraction();
        assertEquals("  áéíóú", messageReplaced);
    }

    @Test
    void builderTest() {
        UnicodeCheck.Builder builder = UnicodeCheck.builder()
            .characters('ñ');

        assertTrue(builder.charMode(CharMode.BLACKLIST).build()
            .check("hola a todos ñ")
            .thenApply(Result::isInfraction)
            .join());
        assertFalse(builder.charMode(CharMode.WHITELIST).build()
            .check("hello everyone ñ")
            .thenApply(Result::isInfraction)
            .join());
    }

    @Test
    void testDefaultCharMethod() {
        char[] chars = {'a', 'h', 'b', 'g', 'e', 'd', 'l'};
        for(final char c : chars) {
            assertFalse(UnicodeCheck.defaultCharTest(c));
        }
    }
}
