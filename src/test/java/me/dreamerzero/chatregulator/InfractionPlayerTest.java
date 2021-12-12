package me.dreamerzero.chatregulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;
import static me.dreamerzero.chatregulator.enums.InfractionType.*;

public class InfractionPlayerTest {
    static InfractionPlayer player;

    @BeforeAll
    static void createPlayer(){
        Logger logger = LoggerFactory.getLogger(InfractionPlayerTest.class);
        Configuration.loadConfig(Paths.get("build", "reports", "tests", "test"), logger);
        Player p = mock(Player.class);
        when(p.getUsername()).thenReturn("4drian3d");
        when(p.getUniqueId()).thenReturn(UUID.randomUUID());
        player = InfractionPlayer.get(p);
    }

    @Test
    @DisplayName("Player Violation Count")
    void countTest(){
        ViolationCount count = player.getViolations();

        count.addViolation(REGULAR);
        count.addViolation(FLOOD);

        assertEquals(1, count.getCount(REGULAR));
        assertEquals(1, count.getCount(FLOOD));

        count.resetViolations(REGULAR, FLOOD);

        assertEquals(0, count.getCount(REGULAR));
        assertEquals(0, count.getCount(FLOOD));
    }

    @Test
    @DisplayName("General Tests")
    void generalTest(){
        assertTrue(player.isOnline());

        assertEquals("4drian3d", player.username());
    }
}
