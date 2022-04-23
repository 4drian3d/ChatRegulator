package me.dreamerzero.chatregulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.utils.TestsUtils;

import static me.dreamerzero.chatregulator.enums.InfractionType.*;

public final class InfractionPlayerTest {

    @BeforeAll
    static void createPlayer(){
        Logger logger = LoggerFactory.getLogger(InfractionPlayerTest.class);
        Configuration.loadConfig(Path.of("build", "reports", "tests", "test"), logger);
    }

    @Test
    @DisplayName("Player Violation Count")
    void countTest(){
        InfractionPlayer player = InfractionPlayer.get(TestsUtils.createNormalPlayer("4drian3d"));
        ViolationCount count = player.getViolations();

        count.addViolation(REGULAR);
        count.addViolation(FLOOD);

        assertEquals(1, count.getCount(REGULAR));
        assertEquals(1, count.getCount(FLOOD));

        count.resetViolations(REGULAR, FLOOD);

        assertEquals(0, count.getCount(REGULAR));
        assertEquals(0, count.getCount(FLOOD));

        assertTrue(player.isOnline());

        assertEquals("4drian3d", player.username());
    }
}
