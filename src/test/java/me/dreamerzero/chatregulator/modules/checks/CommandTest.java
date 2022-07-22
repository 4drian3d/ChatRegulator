package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommandTest {
    @Test
    @DisplayName("Command Check")
    void blockedCommandsTest(){
        String command = "execute";

        assertTrue(CommandCheck.builder()
            .blockedCommands("execute")
            .build()
            .check(command).join().isInfraction());
    }
}
