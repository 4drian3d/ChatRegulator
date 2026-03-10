package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.FloodCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class FloodTest {
    @ParameterizedTest
    @CsvSource({
        "aa floOoOOOooOod aa",
        "helloooooooooooooo"
    })
    @DisplayName("Flood Check")
    void floodCheck(String original) {
        CheckResult result = FloodCheck.builder()
                .limit(5)
                .controlType(ControlType.BLOCK)
                .build()
                .check(TestsUtils.dummyPlayer(), original);
        assertTrue(result.isDenied());
    }

    @Test
    @DisplayName("MultiFlood Replacement")
    void multiFlood() {
        String original = "helloooooo everyoneeeeeee";
        FloodCheck check = FloodCheck.builder()
                .controlType(ControlType.REPLACE)
                .limit(5)
                .build();

        final var result = check.check(TestsUtils.dummyPlayer(), original);
        assertTrue(result.shouldModify());
        final var replaceableResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals("hello everyone", replaceableResult.replaced());
    }

    @ParameterizedTest
    @CsvSource({
            "gaaaaaaaa causa, ga causa",
            "helllllllllllllllllllllllllllllllllllllllllllllo, helo",
            "holabcdefghijkaaaaaaabc, holabcdefghijkabc",
            "plsnoplsnoplsnoplsnoplsnoplsnoplsnoplasnoplsno, plsnoplasnoplsno"
    })
    void testTest(String original, String expected) {
        final FloodCheck check = FloodCheck.builder()
                .controlType(ControlType.REPLACE)
                .limit(5)
                .build();

        CheckResult.ReplaceCheckResult result = assertInstanceOf(CheckResult.ReplaceCheckResult.class, check.check(TestsUtils.dummyPlayer(), original));

        assertEquals(expected, result.replaced());
    }

    @ParameterizedTest
    @CsvSource({
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
            " ҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉҉ ҉"
    })
    void illegalCharsFloodTest(String str) {
        final FloodCheck check = FloodCheck.builder()
                .controlType(ControlType.BLOCK)
                .limit(3)
                .build();
        final CheckResult result = check.check(TestsUtils.dummyPlayer(), str);
        assertTrue(result.isDenied());
    }

    @ParameterizedTest
    @CsvSource({
            "pls no",
            "aplsnopblsnoplcsnoplsdnoplsenoplsnfopalsnopblsnoplscno",
            "yeahnosequemásponeraquiparaquenolodetecte"
    })
    void shouldNotDetectTest(String string) {
        final FloodCheck check = FloodCheck.builder()
                .controlType(ControlType.BLOCK)
                .limit(3)
                .build();
        final CheckResult result = check.check(TestsUtils.dummyPlayer(), string);
        assertTrue(result.isAllowed());
    }
}
