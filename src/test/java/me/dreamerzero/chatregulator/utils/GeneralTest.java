package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.Loader;
import me.dreamerzero.chatregulator.enums.InfractionType;

class GeneralTest {
    @Test
    @DisplayName("General Tests")
    void generaltest(@TempDir Path path){
        Player opplayer = TestsUtils.createOperatorPlayer("OPPlayer");
        Player notOpPlayer = TestsUtils.createNormalPlayer("NotOpPlayer");
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(GeneralTest.class));

        assertTrue(GeneralUtils.allowedPlayer(notOpPlayer, InfractionType.SYNTAX, config));

        assertFalse(GeneralUtils.allowedPlayer(opplayer, InfractionType.FLOOD, config));
    }
}
