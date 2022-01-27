package me.dreamerzero.chatregulator.modules.checks;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;

public class UnicodeTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(UnicodeTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Legal Check")
    void legalCheck(){
        String legal = "ñandú";

        UnicodeCheck uCheck = new UnicodeCheck();

        uCheck.check(legal);

        assertFalse(uCheck.isInfraction());
    }

    @Test
    @DisplayName("Illegal Check")
    void illegalTest(){
        String illegal = "ƕƘaea";

        UnicodeCheck uCheck = new UnicodeCheck();

        uCheck.check(illegal);

        assertTrue(uCheck.isInfraction());

        String replaced = uCheck.replaceInfraction();
        String expected = "  aea";

        assertEquals(expected, replaced);
    }
}
