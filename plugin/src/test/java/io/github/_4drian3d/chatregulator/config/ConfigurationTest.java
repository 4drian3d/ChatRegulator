package io.github._4drian3d.chatregulator.config;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.WarningType;

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
