package me.dreamerzero.chatregulator.modules;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.dreamerzero.chatregulator.config.Configuration;

public final class ReplacerTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(ReplacerTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("First Letter Uppercase")
    void firstLetterUppercase(){
        String original = "peruviankkit";
        String expected = "Peruviankkit";

        String replaced = Replacer.firstLetterUpercase(original);
        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Final Dot")
    void finalDot(){
        String original = "peruviankkit";
        String expected = "peruviankkit.";

        String replaced = Replacer.addFinalDot(original);

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Full Format")
    void applyFullFormat(){
        String original = "peruviankkit";
        String expected = "Peruviankkit.";

        String replaced = Replacer.applyFormat(original);

        assertEquals(replaced, expected);
    }
}
