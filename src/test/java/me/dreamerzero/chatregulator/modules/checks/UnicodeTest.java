package me.dreamerzero.chatregulator.modules.checks;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Loader;
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck.CharMode;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.TestsUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils.EventBundle;

class UnicodeTest {
    @TempDir Path path;

    @Test
    @DisplayName("Illegal Check")
    void illegalTest(){
        String illegal = "ƕƘaea";
        String expected = "  aea";

        UnicodeCheck check = UnicodeCheck.builder()
            .controlType(ControlType.REPLACE)
            .build();
        Result result = check.check(illegal).join();

        assertTrue(result.isInfraction());

        IReplaceable replaceable = assertInstanceOf(IReplaceable.class, result);

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

    @ParameterizedTest
    @ValueSource(strings = {"Hello my friends", "hola, como van?"})
    void legalCheck(String string){
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(UnicodeTest.class));

        assertFalse(UnicodeCheck.createCheck(string, config).join().isInfraction());
    }

    @Test
    void realTest(){
        String randomMSG = "ƕƘáéíóú";
        Player player = TestsUtils.createRandomNormalPlayer();
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(UnicodeTest.class));

        assertTrue(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE, config));

        UnicodeCheck check = UnicodeCheck.builder()
            .controlType(ControlType.REPLACE)
            .build();
        Result result = check.check(randomMSG).join();

        assertTrue(GeneralUtils.callViolationEvent(
            new EventBundle(
                InfractionPlayer.get(player),
                randomMSG,
                InfractionType.UNICODE,
                result, SourceType.CHAT
            ), TestsUtils.createRegulator(path)));
        
        IReplaceable replaceableResult = assertInstanceOf(IReplaceable.class, result);

        String messageReplaced = replaceableResult.replaceInfraction();
        assertEquals("  áéíóú", messageReplaced);
    }

    @ParameterizedTest
    @ValueSource(strings = {"todos los años", "ñandu hahahaha"})
    void builderTest(String msg) {
        UnicodeCheck.Builder builder = UnicodeCheck.builder()
            .characters('ñ');

        assertTrue(builder.charMode(CharMode.BLACKLIST).build()
            .check(msg)
            .thenApply(Result::isInfraction)
            .join());
        assertFalse(builder.charMode(CharMode.WHITELIST).build()
            .check(msg)
            .thenApply(Result::isInfraction)
            .join());
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'h', 'b', 'g', 'e', 'd', 'l'})
    void testDefaultCharMethod(char character) {
        assertFalse(UnicodeCheck.defaultCharTest(character));
    }
}
