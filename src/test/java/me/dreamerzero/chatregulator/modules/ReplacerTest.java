package me.dreamerzero.chatregulator.modules;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.dreamerzero.chatregulator.config.Configuration;

public class ReplacerTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(ReplacerTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("First Letter Uppercase")
    void firstLetterUppercase(){
        String original = "peruviankkit";
        String expected = "Peruviankkit";
        Replacer r = new Replacer();

        String replaced = r.firstLetterUpercase(original);
        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Final Dot")
    void finalDot(){
        String original = "peruviankkit";
        String expected = "peruviankkit.";

        Replacer r = new Replacer();

        String replaced = r.addFinalDot(original);

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Full Format")
    void applyFullFormat(){
        String original = "peruviankkit";
        String expected = "Peruviankkit.";

        Replacer r = new Replacer();

        String replaced = r.applyFormat(original);

        assertEquals(replaced, expected);
    }
}
