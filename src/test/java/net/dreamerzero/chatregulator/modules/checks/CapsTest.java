package net.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamerzero.chatregulator.config.Configuration;

public class CapsTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(CapsTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Caps Test")
    void capsTest(){
        String original = "HELLO EVERYONE";
        String expected = "hello everyone";

        CapsCheck cCheck = new CapsCheck();

        cCheck.check(original);
        String replaced = cCheck.replaceInfraction();

        assertTrue(cCheck.isInfraction());
        assertEquals(expected, replaced);
    }
}
