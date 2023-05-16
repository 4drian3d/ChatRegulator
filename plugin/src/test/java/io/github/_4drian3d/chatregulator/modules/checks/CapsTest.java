package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.CapsCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.plugin.config.Checks;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CapsTest {
    @Test
    @DisplayName("Caps Test")
    void capsTest(){
        String original = "HELLO EVERYONE";
        String expected = "hello everyone";

        CapsCheck check = CapsCheck.builder()
                .limit(5)
                .controlType(ControlType.REPLACE)
                .build();
        CheckResult result = check.check(TestsUtils.dummyPlayer(), original);
        assertTrue(result.shouldModify());
        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals(expected, replaceResult.replaced());
    }

    @Test
    void realTest(@TempDir Path path){
        String message = "AAAAAAAAAA";

        Checks config = ConfigurationContainer.load(LoggerFactory.getLogger(CapsTest.class), path, Checks.class, "config").get();

        CapsCheck check = CapsCheck.builder()
                .limit(config.getCapsConfig().limit())
                .controlType(ControlType.REPLACE)
                .build();
        CheckResult result = check.check(TestsUtils.dummyPlayer(), message);
        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals("aaaaaaaaaa", replaceResult.replaced());
    }
}
