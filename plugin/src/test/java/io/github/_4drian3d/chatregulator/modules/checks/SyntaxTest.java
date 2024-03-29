package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.SyntaxCheck;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SyntaxTest {
    @ParameterizedTest
    @CsvSource({
        "minecraft:ban 4drian3d fuck",
        "worldedit:calc 1+1"
    })
    @DisplayName("Syntax Blocker Check")
    void syntaxCheck(String command){
        SyntaxCheck check = SyntaxCheck.builder().build();
        assertTrue(check.check(TestsUtils.dummyPlayer(), command).isDenied());
    }

    @Test
    void allowedCommand() {
        String command = "minecraft:tell 4drian3d hola!";
        SyntaxCheck check = SyntaxCheck.builder()
                .allowedCommands(List.of("minecraft"))
                .build();

        assertTrue(check.check(TestsUtils.dummyPlayer(), command).isAllowed());
    }
}
