package me.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.result.IReplaceable;

public class InfractionTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(InfractionTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
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
            .blockedStrings(
                "sh[ilj1y]t",
                "d[ilj1y]c(k)?",
                "f[uv4]ck"
            )
            .build();

        String original = "Hello D1cK sh1t f4ck!!!";
        String expected = "Hello *** *** ***!!!";

        iCheck.check(original).thenAccept(result -> {
            assertTrue(result.isInfraction());
            assertTrue(result instanceof IReplaceable);

            String replaced = ((IReplaceable)result).replaceInfraction();

            assertEquals(expected, replaced);
        }).join();
    }
}
