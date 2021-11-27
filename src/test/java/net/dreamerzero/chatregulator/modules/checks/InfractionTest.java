package net.dreamerzero.chatregulator.modules.checks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.config.Configuration;

public class InfractionTest {
    private static Yaml config = new Yaml("config", "build/reports/tests/test/config");
    private static Yaml blacklist = new Yaml("blacklist", "build/reports/tests/test/config");
    private static Yaml messages = new Yaml("messages", "build/reports/tests/test/config");
    @BeforeAll
    static void loadConfig(){
        new Configuration(config, blacklist, messages).setDefaultConfig();
    }

    @Test
    @DisplayName("Check Test")
    void detectiontest(){
        InfractionCheck iCheck = new InfractionCheck(blacklist);
        String original = "asdasdasdadad shit dadasdad";

        iCheck.check(original);

        assertTrue(iCheck.isInfraction());
    }

    @Test
    @DisplayName("Infraction Replacement Test")
    void replaceTest(){
        InfractionCheck iCheck = new InfractionCheck(blacklist);

        String original = "aaa fuck aaa";
        iCheck.check(original);

        String replaced = iCheck.replaceInfraction();
        String expected = "aaa *** aaa";

        assertEquals(expected, replaced);
    }
}
