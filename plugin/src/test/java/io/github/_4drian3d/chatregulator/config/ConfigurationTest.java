package io.github._4drian3d.chatregulator.config;

import java.nio.file.Path;

import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.WarningType;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @Test
    @DisplayName("Config Values")
    void configValues(@TempDir Path path){
        Configuration config = ConfigurationContainer.load(LoggerFactory.getLogger(ConfigurationTest.class), path, Configuration.class, "").get();

        assertTrue(config.getCommandBlacklistConfig().enabled());
        assertFalse(config.getFormatConfig().enabled());

        assertEquals(2, config.getFloodConfig().getCommandsConfig().violationsRequired());
        assertEquals(2, config.getUnicodeConfig().getCommandsConfig().violationsRequired());

        assertEquals(ControlType.BLOCK, config.getInfractionsConfig().getControlType());
        assertEquals(WarningType.MESSAGE, config.getSpamConfig().getWarningType());
    }

}
