package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;

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

        String original = "aa floOoOOOooOod aa";
        String expected = "aa fld aa";

        fCheck.check(original);
        String replaced = fCheck.replaceInfraction();

        assertEquals(replaced, expected);
        assertTrue(fCheck.isInfraction());
    }
}
