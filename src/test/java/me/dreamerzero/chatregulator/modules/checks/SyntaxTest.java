package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public final class SyntaxTest {
    @Test
    @DisplayName("Syntax Blocker Check")
    void syntaxCheck(){
        String command = "minecraft:ban 4drian3d fuck";

        String expectedInfraction = "minecraft:ban";

        assertTrue(SyntaxCheck.createCheck(command).thenApply(result -> {
            assertEquals(expectedInfraction, result.getInfractionString());
            return result.isInfraction();
        }).join());
    }
}
