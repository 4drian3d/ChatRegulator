package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;

public class CommandsTest {
    @Test
    @DisplayName("First Argument")
    void testFirstArgument(){
        String original = "tell 4drian3d hola bb";

        String expectedDetection = "tell";

        String result = CommandUtils.getFirstArgument(original);

        assertEquals(expectedDetection, result);
    }

    @Test
    @DisplayName("Command Blacklisted")
    void isCommandBlacklisted(){
        Logger logger = LoggerFactory.getLogger(CommandsTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);

        String command = "execute for all";
        boolean isCommand = CommandUtils.isCommand(command);

        assertTrue(isCommand);
    }

    @Test
    @DisplayName("Starting String")
    void isStartingCommand(){
        String firstcommand = "lp group Owner";
        String firstconfig = "lp group";

        assertTrue(CommandUtils.isStartingString(firstcommand, firstconfig));

        String secondcommand = "lp group";
        String secondconfusion = "lpermission";
        String secondconfig = "lp";

        assertTrue(CommandUtils.isStartingString(secondcommand, secondconfig));
        assertFalse(CommandUtils.isStartingString(secondconfusion, secondconfig));

        String thirdcommand = "lppermissions";

        String thirdconfig = "lp*";

        assertTrue(CommandUtils.isStartingString(thirdcommand, thirdconfig));
    }
}
