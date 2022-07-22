package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CommandTest {
    private final CommandCheck check = CommandCheck.builder()
        .blockedCommands("execute", "stop")
        .build();

    @ParameterizedTest
    @ValueSource(strings = {"execute", "stop my server"})
    @DisplayName("Command Check")
    void blockedCommandsTest(String command){
        assertTrue(check.check(command).join().isInfraction());
    }
}
