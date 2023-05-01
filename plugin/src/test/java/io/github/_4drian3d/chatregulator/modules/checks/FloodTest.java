package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.FloodCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FloodTest {
    @ParameterizedTest
    @CsvSource({
        "aa floOoOOOooOod aa, aa flod aa",
        "helloooooooooooooo, hello"
    })
    @DisplayName("Flood Check")
    void floodCheck(String original, String expected) {
        CheckResult result = FloodCheck.builder()
                .limit(5)
                .controlType(ControlType.BLOCK)
                .build()
                .check(TestsUtils.dummyPlayer(), original);
        assertTrue(result.isDenied());
    }

    @Test
    @DisplayName("MultiFlood Replacement")
    void multiFlood(){
        String original = "helloooooo everyoneeeeeee";
        FloodCheck check = FloodCheck.builder()
                .controlType(ControlType.REPLACE)
                .limit(5)
                .build();

        assertTrue(check.check(TestsUtils.dummyPlayer(), original).shouldModify());
    }
}
