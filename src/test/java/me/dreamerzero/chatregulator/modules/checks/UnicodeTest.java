package me.dreamerzero.chatregulator.modules.checks;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.result.IReplaceable;

public class UnicodeTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(UnicodeTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Legal Check")
    void legalCheck(){
        String legal = "ñandú";

        UnicodeCheck uCheck = new UnicodeCheck();

        uCheck.check(legal).thenAccept(result -> {
            assertFalse(result.isInfraction());
        });
    }

    @Test
    @DisplayName("Illegal Check")
    void illegalTest(){
        String illegal = "ƕƘaea";

        UnicodeCheck uCheck = new UnicodeCheck();

        uCheck.check(illegal).thenAccept(result -> {
            assertTrue(result.isInfraction());

            assertTrue(result instanceof IReplaceable);

            String replaced = ((IReplaceable)result).replaceInfraction();
            String expected = "  aea";

            assertEquals(expected, replaced);
        });
    }
}
