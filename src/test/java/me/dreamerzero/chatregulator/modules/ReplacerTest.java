package me.dreamerzero.chatregulator.modules;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.dreamerzero.chatregulator.config.Configuration;

class ReplacerTest {
    @BeforeAll
    static void loadConfig(@TempDir Path path){
        Configuration.loadConfig(path, LoggerFactory.getLogger(ReplacerTest.class));
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
