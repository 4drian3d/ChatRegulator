package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SyntaxTest {
    @ParameterizedTest
    @CsvSource({
        "minecraft:ban 4drian3d fuck, minecraft:ban",
        "worldedit:calc 1+1, worldedit:calc"
    })
    @DisplayName("Syntax Blocker Check")
    void syntaxCheck(String command, String expectedInfraction){
        assertTrue(SyntaxCheck.createCheck(command).thenApply(result -> {
            assertEquals(expectedInfraction, result.getInfractionString());
            return result.isInfraction();
        }).join());
    }
}
