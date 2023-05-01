package io.github._4drian3d.chatregulator.modules.checks;


import io.github._4drian3d.chatregulator.api.checks.UnicodeCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

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
        CheckResult result = check.check(TestsUtils.dummyPlayer(), illegal);

        assertTrue(result.isDenied());
        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals(expected, replaceResult.replaced());
    }

    @Test
    @DisplayName("Custom Check")
    void custom(){
        String illegal = "ñn't";

        var result = UnicodeCheck.builder()
            .characters('ñ')
            .build()
            .check(TestsUtils.dummyPlayer(), illegal);

        assertTrue(result.isDenied());
    }

    @Test
    void realTest(@TempDir Path path){
        String randomMSG = "ƕƘáéíóú";
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        InfractionPlayerImpl player = TestsUtils.createRandomNormalPlayer(plugin);

        assertTrue(player.isAllowed(InfractionType.UNICODE));

        UnicodeCheck check = UnicodeCheck.builder()
            .controlType(ControlType.REPLACE)
            .build();
        CheckResult result = check.check(TestsUtils.dummyPlayer(), randomMSG);
        assertTrue(result.shouldModify());
        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals("  áéíóú", replaceResult.replaced());
    }

    @ParameterizedTest
    @ValueSource(strings = {"todos los años", "ñandu hahahaha"})
    void builderTest(String msg) {
        UnicodeCheck.Builder builder = UnicodeCheck.builder()
            .characters('ñ');

        assertTrue(builder.charMode(DetectionMode.BLACKLIST).build()
            .check(TestsUtils.dummyPlayer(), msg).isAllowed());
        assertFalse(builder.charMode(DetectionMode.WHITELIST).build()
            .check(TestsUtils.dummyPlayer(), msg).isAllowed());
    }

    @ParameterizedTest
    @ValueSource(chars = {'a', 'h', 'b', 'g', 'e', 'd', 'l'})
    void testDefaultCharMethod(char character) {
        assertFalse(UnicodeCheck.defaultCharTest(character));
    }
}
