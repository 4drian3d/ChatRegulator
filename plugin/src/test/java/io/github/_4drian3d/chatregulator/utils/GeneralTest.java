package io.github._4drian3d.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.Loader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

class GeneralTest {
    @Test
    @DisplayName("General Tests")
    void generaltest(@TempDir Path path) {
        Player opplayer = TestsUtils.createOperatorPlayer("OPPlayer");
        Player notOpPlayer = TestsUtils.createNormalPlayer("NotOpPlayer");
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(GeneralTest.class));
        ChatRegulator plugin = TestsUtils.createRegulator(path);

        assertTrue(utils.allowedPlayer(notOpPlayer, InfractionType.SYNTAX, config));

        assertFalse(utils.allowedPlayer(opplayer, InfractionType.FLOOD, config));
    }
}
