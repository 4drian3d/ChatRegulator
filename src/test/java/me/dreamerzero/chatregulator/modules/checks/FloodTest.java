package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.result.IReplaceable;

public class FloodTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(FloodTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Flood Check")
    void floodCheck(){
        String original = "aa floOoOOOooOod aa";
        String expected = "aa fld aa";

        FloodCheck.createCheck(original).thenAccept(result->{
            assertTrue(result.isInfraction());
            assertTrue(result instanceof IReplaceable);
            String replaced = ((IReplaceable)result).replaceInfraction();

            assertEquals(replaced, expected);
        });
    }
}
