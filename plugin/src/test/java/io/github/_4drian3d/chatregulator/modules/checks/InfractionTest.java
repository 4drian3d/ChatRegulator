package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.InfractionCheck;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.nio.file.Path;
import java.util.regex.Pattern;

import io.github._4drian3d.chatregulator.plugin.ChatRegulator;

public final class InfractionTest {

    @Test
    @DisplayName("Check Test")
    void detectionTest(@TempDir Path path){
        String original = "asdasdasdadadSh1T dadasdad";
        ChatRegulator plugin = TestsUtils.createRegulator(path);

        assertTrue(InfractionCheck.createCheck(original, plugin).join().isInfraction());
    }

    // Test correct pattern order
    @RepeatedTest(3)
    @DisplayName("Replacement Test")
    void replaceMultiple(){
        InfractionCheck iCheck = InfractionCheck.builder()
            .replaceable(true)
            .blockedPatterns(
                Pattern.compile("sh[ilj1y]t", Pattern.CASE_INSENSITIVE),
                Pattern.compile("d[ilj1y]ck", Pattern.CASE_INSENSITIVE),
                Pattern.compile("m[0o]th[e3]rf[uv]ck[e3]r", Pattern.CASE_INSENSITIVE),
                Pattern.compile("f[uv4]ck", Pattern.CASE_INSENSITIVE)
            )
            .build();

        String original = "Hello D1cK sh1t f4ck mOtherfVck3r!!!";
        String expected = "Hello ** ** ** ******!!!";

        var check = iCheck.check(original).join();
        assertTrue(check.isInfraction());
        /*IReplaeable replaceable = assertInstanceOf(IReplceable.class, check);
        String replaced = replaceable.replaceInfraction();

        assertEquals(expected, replaced);*/
    }

    @Test
    void testReplacement() {
        final Pattern testPattern = Pattern.compile("f[uv]ck[e3]rs");
        final String original = "Hello fuckers";
        final String expected = "Hello ***";

        final String actual = testPattern.matcher(original)
            .replaceAll(InfractionCheck::generateReplacement);

        assertEquals(expected, actual);

    }
}
