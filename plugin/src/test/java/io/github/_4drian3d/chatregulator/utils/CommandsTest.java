package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.api.utils.Commands;
import io.github._4drian3d.chatregulator.common.configuration.Blacklist;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {
    @ParameterizedTest(name = "{0}'s first argument is tell")
    @ValueSource(strings = {"tell 4drian3d hola bb", "tell"})
    @DisplayName("First Argument")
    void testFirstArgument(String original){
        final String expectedDetection = "tell";

        String result = Commands.getFirstArgument(original);

        assertEquals(expectedDetection, result);
    }

    @Test
    @DisplayName("Command Blacklisted")
    void isCommandBlacklisted(@TempDir Path path){
        Blacklist config = ConfigurationContainer.load(LoggerFactory.getLogger(CommandsTest.class), path,  Blacklist.class,"").get();

        String command = "execute for all";
        boolean isCommand = config.isBlockedCommand(command);

        assertTrue(isCommand);
    }

    @ParameterizedTest
    @CsvSource({
        "lp group Owner, lp group",
        "lp group, lp",
        "lppermissions, lp*"
    })
    @DisplayName("Starting String")
    void isStartingCommand(String command, String config){
        assertTrue(Commands.isStartingString(command, config));
    }

    @Test
    @DisplayName("Non starting string")
    void notStartingString() {
        String command = "lpermission";
        String config = "lp";
        assertFalse(Commands.isStartingString(command, config));
    }

    @ParameterizedTest(name = "{0}'s last character is '!'")
    @ValueSource(strings = {"Ho laa aa aa a !", "!", "!!!! !!!!! !"})
    @DisplayName("Last Char")
    void lastChar(String string) {
        final char lastChar = '!';

        final char theLastChar = Commands.getLastChar(string);
        assertEquals(lastChar, theLastChar);
    }
}
