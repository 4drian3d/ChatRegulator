package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Blacklist;
import me.dreamerzero.chatregulator.config.Loader;

class CommandsTest {
    @ParameterizedTest(name = "{0}'s first argument is tell")
    @ValueSource(strings = {"tell 4drian3d hola bb", "tell"})
    @DisplayName("First Argument")
    void testFirstArgument(String original){
        final String expectedDetection = "tell";

        String result = CommandUtils.getFirstArgument(original);

        assertEquals(expectedDetection, result);
    }

    @Test
    @DisplayName("Command Blacklisted")
    void isCommandBlacklisted(@TempDir Path path){
        Blacklist config = Loader.loadBlacklistConfig(path, LoggerFactory.getLogger(CommandsTest.class));

        String command = "execute for all";
        boolean isCommand = CommandUtils.isCommand(command, config);

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
        assertTrue(CommandUtils.isStartingString(command, config));
    }

    @Test
    @DisplayName("Not starting string")
    void notStartingString() {
        String command = "lpermission";
        String config = "lp";
        assertFalse(CommandUtils.isStartingString(command, config));
    }

    @ParameterizedTest(name = "{0}'s last character is '!'")
    @ValueSource(strings = {"Holaaaaaaa!", "!", "!!!!!!!!!!"})
    @DisplayName("Last Char")
    void lastChar(String string) {
        final char lastChar = '!';

        final char theLastChar = CommandUtils.getLastChar(string);
        assertEquals(lastChar, theLastChar);
    }
}
