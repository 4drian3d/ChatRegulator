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
        String original = "asdasdasdadadSh1T dadasdad";

        iCheck.check(original);

        assertTrue(iCheck.isInfraction());
    }

    @Test
    @DisplayName("Replacement Test")
    void replaceMultiple(){
        InfractionCheck iCheck = new InfractionCheck();

        String original = "Hello D1cK sh1t f4ck!!!";
        String expected = "Hello *** *** ***!!!";

        iCheck.check(original);
        String replaced = iCheck.replaceInfractions();

        assertEquals(expected, replaced);
    }
}
