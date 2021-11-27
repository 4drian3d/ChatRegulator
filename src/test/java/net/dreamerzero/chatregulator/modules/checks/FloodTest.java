package net.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.Configuration;

public class FloodTest {
    private static Yaml config = new Yaml("config", "build/reports/tests/test/config");
    private static Yaml blacklist = new Yaml("blacklist", "build/reports/tests/test/config");
    private static Yaml messages = new Yaml("messages", "build/reports/tests/test/config");
    @BeforeAll
    static void loadConfig(){
        new Configuration(config, blacklist, messages).setDefaultConfig();
    }

    @Test
    @DisplayName("Flood Check")
    @Disabled("Broken D:")
    //TODO: Fix this test
    void floodCheck(){
        FloodCheck fCheck = new FloodCheck(config);

        String original = "flooooooooooood";

        fCheck.check(original);

        assertTrue(fCheck.isInfraction());
    }

    @Test
    @DisplayName("Flood Replacement Test")
    void replaceFlood(){
        FloodCheck fCheck = new FloodCheck(config);

        String original = "flooooooooooood";

        fCheck.check(original);

        String replaced = fCheck.replaceInfraction();
        String expected = "fld";

        assertEquals(replaced, expected);
    }
}
