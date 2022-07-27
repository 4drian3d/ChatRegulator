package me.dreamerzero.chatregulator.config;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.WarningType;

class ConfigurationTest {
    @Test
    @DisplayName("Config Values")
    void configValues(@TempDir Path path){
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(ConfigurationTest.class));

        assertTrue(config.getCommandBlacklistConfig().enabled());
        assertFalse(config.getFormatConfig().enabled());

        assertEquals(2, config.getFloodConfig().getCommandsConfig().violationsRequired());
        assertEquals(2, config.getUnicodeConfig().getCommandsConfig().violationsRequired());

        assertEquals(ControlType.BLOCK, config.getInfractionsConfig().getControlType());
        assertEquals(WarningType.MESSAGE, config.getSpamConfig().getWarningType());
    }

}
