package net.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamerzero.chatregulator.config.Configuration;

public class FloodTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(FloodTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Flood Check")
    void floodCheck(){
        FloodCheck fCheck = new FloodCheck();

        String original = "aa flooooooooooood aa";

        fCheck.check(original);

        assertTrue(fCheck.isInfraction());
    }

    @Test
    @DisplayName("Replacement Test")
    void replaceFlood(){
        FloodCheck fCheck = new FloodCheck();

        String original = "yee flooooooooooood yee";

        fCheck.check(original);

        String replaced = fCheck.replaceInfraction();
        String expected = "yee fld yee";

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Case Insensitive Test")
    void caseTest(){
        FloodCheck fCheck = new FloodCheck();

        String original = "floOoOoOooOooOd";

        fCheck.check(original);

        assertTrue(fCheck.isInfraction());
    }
}
