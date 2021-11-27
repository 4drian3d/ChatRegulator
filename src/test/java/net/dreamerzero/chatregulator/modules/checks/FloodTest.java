package net.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

import net.dreamerzero.chatregulator.config.Configuration;

public class FloodTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(FloodTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Flood Check")
    @Disabled("Broken D:")
    //TODO: Fix this test
    void floodCheck(){
        FloodCheck fCheck = new FloodCheck();

        String original = "flooooooooooood";

        fCheck.check(original);

        assertTrue(fCheck.isInfraction());
    }

    @Test
    @DisplayName("Flood Replacement Test")
    void replaceFlood(){
        FloodCheck fCheck = new FloodCheck();

        String original = "flooooooooooood";

        fCheck.check(original);

        String replaced = fCheck.replaceInfraction();
        String expected = "fld";

        assertEquals(replaced, expected);
    }
}
