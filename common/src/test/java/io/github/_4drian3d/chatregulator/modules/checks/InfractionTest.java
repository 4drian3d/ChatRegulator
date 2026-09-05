package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.RegexCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.common.configuration.Blacklist;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public final class InfractionTest {

    @Test
    @DisplayName("Check Test")
    void detectionTest(@TempDir Path path){
        String original = "asdasdasdadadSh1T dadasdad";
        Blacklist configuration = ConfigurationContainer.load(LoggerFactory.getLogger(InfractionTest.class), path, Blacklist.class, "blacklist").get();
        RegexCheck check = RegexCheck.builder()
                        .controlType(ControlType.BLOCK)
                        .blockedPatterns(configuration.getBlockedPatterns())
                        .build();

        assertTrue(check.check(TestsUtils.dummyPlayer(), original).isDenied());
    }

    // Test correct pattern order
    @RepeatedTest(3)
    @DisplayName("Replacement Test")
    void replaceMultiple(){
        RegexCheck check = RegexCheck.builder()
            .controlType(ControlType.REPLACE)
            .blockedPatterns(
                Pattern.compile("sh[ilj1y]t", Pattern.CASE_INSENSITIVE),
                Pattern.compile("d[ilj1y]ck", Pattern.CASE_INSENSITIVE),
                Pattern.compile("m[0o]th[e3]rf[uv]ck[e3]r", Pattern.CASE_INSENSITIVE),
                Pattern.compile("f[uv4]ck", Pattern.CASE_INSENSITIVE)
            )
            .build();

        String original = "Hello D1cK sh1t f4ck mOtherfVck3r!!!";
        String expected = "Hello ** ** ** ******!!!";

        CheckResult result = check.check(TestsUtils.dummyPlayer(), original);
        assertTrue(result.shouldModify());
        CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
        assertEquals(expected, replaceResult.replaced());
    }

    @Test
    void testReplacement() {
        final Pattern testPattern = Pattern.compile("f[uv]ck[e3]rs");
        final String original = "Hello fuckers";
        final String expected = "Hello ***";

        final String actual = testPattern.matcher(original)
            .replaceAll(RegexCheck::generateReplacement);

        assertEquals(expected, actual);

    }
}
