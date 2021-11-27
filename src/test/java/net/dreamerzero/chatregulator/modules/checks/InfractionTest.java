package net.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

import net.dreamerzero.chatregulator.config.Configuration;

public class InfractionTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(FloodTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Check Test")
    void detectiontest(){
        InfractionCheck iCheck = new InfractionCheck();
        String original = "asdasdasdadad shit dadasdad";

        iCheck.check(original);

        assertTrue(iCheck.isInfraction());
    }

    @Test
    @DisplayName("Infraction Replacement Test")
    void replaceTest(){
        InfractionCheck iCheck = new InfractionCheck();

        String original = "aaa fuck aaa";
        iCheck.check(original);

        String replaced = iCheck.replaceInfraction();
        String expected = "aaa *** aaa";

        assertEquals(expected, replaced);
    }
}
