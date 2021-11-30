package net.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import net.dreamerzero.chatregulator.config.Configuration;

public class InfractionTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(InfractionTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Check Test")
    void detectionTest(){
        InfractionCheck iCheck = new InfractionCheck();
        String original = "asdasdasdadad sh1t dadasdad";

        iCheck.check(original);

        assertTrue(iCheck.isInfraction());
    }

    @Test
    @DisplayName("Replacement Test")
    void replaceTest(){
        InfractionCheck iCheck = new InfractionCheck();

        String original = "aaa f4ck aaa";
        iCheck.check(original);

        String replaced = iCheck.replaceInfraction();
        String expected = "aaa *** aaa";

        assertEquals(expected, replaced);
    }

    @Test
    @DisplayName("Case Insensitive Test")
    void caseTest(){
        InfractionCheck iCheck = new InfractionCheck();

        String original = "D1cK";

        iCheck.check(original);

        assertTrue(iCheck.isInfraction());
    }

    @Test
    @DisplayName("Multi Replace")
    void replaceMultiple(){
        InfractionCheck iCheck = new InfractionCheck();

        String original = "D1cK sh1t f4ck";
        String expected = "* * *";

        iCheck.check(original);

        assertEquals(expected, iCheck.replaceInfractionss());
    }
}
