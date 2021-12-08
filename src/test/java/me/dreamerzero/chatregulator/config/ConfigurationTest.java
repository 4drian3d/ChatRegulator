package me.dreamerzero.chatregulator.config;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.WarningType;

public class ConfigurationTest {
    @BeforeAll
    static void loadConfig(){
        Logger logger = LoggerFactory.getLogger(ConfigurationTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Config Values")
    void configValues(){
        var config = Configuration.getConfig();

        assertTrue(config.getCommandBlacklistConfig().enabled());
        assertFalse(config.getFormatConfig().enabled());

        assertEquals(2, config.getFloodConfig().getCommandsConfig().violationsRequired());
        assertEquals(2, config.getUnicodeConfig().getCommandsConfig().violationsRequired());

        assertEquals(ControlType.BLOCK, config.getInfractionsConfig().getControlType());
        assertEquals(WarningType.MESSAGE, config.getSpamConfig().getWarningType());
    }

}
