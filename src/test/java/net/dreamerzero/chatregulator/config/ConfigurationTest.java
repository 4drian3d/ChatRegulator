package net.dreamerzero.chatregulator.config;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(ConfigurationTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Config Values")
    void configValues(){
        assertTrue(Configuration.getConfig().getCommandBlacklistConfig().enabled());
        assertFalse(Configuration.getConfig().getFormatConfig().enabled());
    }

    @Test
    @DisplayName("Extension Test")
    void commonCommandsConfig(){
        assertEquals(2, Configuration.getConfig().getFloodConfig().getCommandsConfig().violationsRequired());
        assertEquals(2, Configuration.getConfig().getUnicodeConfig().getCommandsConfig().violationsRequired());
    }
}
