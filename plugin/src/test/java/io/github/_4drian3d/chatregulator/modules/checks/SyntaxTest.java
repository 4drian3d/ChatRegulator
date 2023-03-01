package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.SyntaxCheck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
