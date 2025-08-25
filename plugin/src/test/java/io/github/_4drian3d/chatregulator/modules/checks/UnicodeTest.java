package io.github._4drian3d.chatregulator.modules.checks;


import io.github._4drian3d.chatregulator.api.checks.UnicodeCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UnicodeTest {
    @Test
    @DisplayName("Character Check")
    void character() {
        String illegal = "ñn't";
        String expected = "n't";

        var builder = UnicodeCheck.builder()
                .characters((int) 'ñ')
                .charDetectionMode(DetectionMode.BLACKLIST);

        assertTrue(builder.charControlType(ControlType.BLOCK)
                .build().check(TestsUtils.dummyPlayer(), illegal).isDenied());

        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class,
                builder.charControlType(ControlType.REPLACE).build().check(TestsUtils.dummyPlayer(), illegal));
        assertTrue(replaceResult.shouldModify());
        assertEquals(expected, replaceResult.replaced());
    }

    @Test
    @DisplayName("Unicode Block Check")
    void blockTest() {
        String illegal = "ƕƘaea";
        String expected = "aea";

        UnicodeCheck check = UnicodeCheck.builder()
                .blocks(Character.UnicodeBlock.LATIN_EXTENDED_B)
                .blockControlType(ControlType.REPLACE)
                .blockDetectionMode(DetectionMode.BLACKLIST)
                .build();
        CheckResult result = check.check(TestsUtils.dummyPlayer(), illegal);

        assertTrue(result.shouldModify());

        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals(expected, replaceResult.replaced());
    }

    @Test
    @DisplayName("Unicode Script Check")
    void scriptTest() {
        String illegal = "\uD83D\uDE04\u2182#\u21D4\u2CC3\u250E\u23E9\u28BD\u25D7";
        String expected = "\u2182\u2CC3\u28BD";

        UnicodeCheck check = UnicodeCheck.builder()
                .scripts(Character.UnicodeScript.COMMON)
                .scriptControlType(ControlType.REPLACE)
                .scriptDetectionMode(DetectionMode.BLACKLIST)
                .build();
        CheckResult result = check.check(TestsUtils.dummyPlayer(), illegal);

        assertTrue(result.shouldModify());

        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals(expected, replaceResult.replaced());
    }

    @ParameterizedTest
    @ValueSource(strings = {"todos los años", "ñandu hahahaha"})
    void builderTest(String msg) {
        UnicodeCheck.Builder builder = UnicodeCheck.builder().charControlType(ControlType.BLOCK);

        assertTrue(builder.characters((int) 'ñ').charDetectionMode(DetectionMode.BLACKLIST).build()
                .check(TestsUtils.dummyPlayer(), msg).isDenied());
        assertFalse(builder.characters("dhanolstuñ ".chars().boxed().toArray(Integer[]::new))
                .charDetectionMode(DetectionMode.WHITELIST).build()
                .check(TestsUtils.dummyPlayer(), msg).isDenied());
    }
}
