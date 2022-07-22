package me.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.regex.Pattern;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.result.IReplaceable;

public final class InfractionTest {
    @BeforeAll
    static void loadConfig(@TempDir Path path){
        Configuration.loadConfig(path, LoggerFactory.getLogger(InfractionTest.class));
    }

    @Test
    @DisplayName("Check Test")
    void detectionTest(){
        String original = "asdasdasdadadSh1T dadasdad";

        assertTrue(InfractionCheck.createCheck(original).join().isInfraction());
    }

    @Test
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
        assertTrue(check instanceof IReplaceable);

        String replaced = ((IReplaceable)check).replaceInfraction();

        assertEquals(expected, replaced);
    }
}
