package net.dreamerzero.chatregulator.modules.checks;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamerzero.chatregulator.config.Configuration;

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
        String illegal = "ƕƘ";

        UnicodeCheck uCheck = new UnicodeCheck();

        uCheck.check(illegal);

        assertTrue(uCheck.isInfraction());
    }
}
