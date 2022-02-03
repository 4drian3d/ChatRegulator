package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;

public class GeneralTest {
    @Test
    @DisplayName("General Tests")
    void generaltest(){
        Player opplayer = TestsUtils.createOperatorPlayer("OPPlayer");
        Player notOpPlayer = TestsUtils.createNormalPlayer("NotOpPlayer");

        assertTrue(GeneralUtils.allowedPlayer(notOpPlayer, Configuration.getConfig().getSyntaxConfig(), InfractionType.SYNTAX));

        assertFalse(GeneralUtils.allowedPlayer(opplayer, Configuration.getConfig().getFloodConfig(), InfractionType.FLOOD));
    }
}
