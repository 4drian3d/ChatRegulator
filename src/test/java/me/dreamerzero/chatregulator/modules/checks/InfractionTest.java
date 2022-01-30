package me.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.result.PatternReplaceableResult;
import me.dreamerzero.chatregulator.result.ReplaceableResult;

public class InfractionTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(InfractionTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Check Test")
    void detectionTest(){
        InfractionCheck iCheck = InfractionCheck.builder().replaceable(true).build();
        String original = "asdasdasdadadSh1T dadasdad";

        iCheck.check(original).thenAccept(result -> {
            assertTrue(result instanceof ReplaceableResult);
        });
    }

    @Test
    @DisplayName("Replacement Test")
    void replaceMultiple(){
        InfractionCheck iCheck = InfractionCheck.builder().replaceable(true).build();

        String original = "Hello D1cK sh1t f4ck!!!";
        String expected = "Hello *** *** ***!!!";

        iCheck.check(original).thenAccept(result -> {
            assertTrue(result.isInfraction());
            boolean isInstanceOfReplaceable = result instanceof PatternReplaceableResult;
            assertTrue(isInstanceOfReplaceable);

            String replaced = ((PatternReplaceableResult)result).replaceInfraction();

            assertEquals(expected, replaced);
        });

        
    }
}
