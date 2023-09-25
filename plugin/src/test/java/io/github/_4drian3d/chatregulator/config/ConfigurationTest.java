package io.github._4drian3d.chatregulator.config;

import java.nio.file.Path;

import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
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
        Checks checks = ConfigurationContainer.load(LoggerFactory.getLogger(ConfigurationTest.class), path, Checks.class, "test").get();
        Configuration config = ConfigurationContainer.load(LoggerFactory.getLogger(ConfigurationTest.class), path, Configuration.class, "config").get();

        assertTrue(checks.getCommandBlacklistConfig().enabled());
        assertFalse(config.getFormatterConfig().enabled());

        assertEquals(2, checks.getFloodConfig().getCommandsConfig().violationsRequired());
        assertEquals(2, checks.getUnicodeConfig().getCommandsConfig().violationsRequired());

        assertEquals(ControlType.BLOCK, checks.getRegexConfig().getControlType());
        assertEquals(WarningType.MESSAGE, checks.getSpamConfig().getWarningType());
    }

}
