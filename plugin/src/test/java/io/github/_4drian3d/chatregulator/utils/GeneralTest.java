package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneralTest {
    @Test
    @DisplayName("General Tests")
    void generaltest(@TempDir Path path) {
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        InfractionPlayerImpl opPlayer = TestsUtils.createOperatorPlayer("OPPlayer", plugin);
        InfractionPlayerImpl notOpPlayer = TestsUtils.createNormalPlayer("NotOpPlayer", plugin);


        assertTrue(notOpPlayer.isAllowed(InfractionType.SYNTAX));

        assertFalse(opPlayer.isAllowed(InfractionType.FLOOD));
    }
}
