package me.dreamerzero.chatregulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.utils.TestsUtils;

import static me.dreamerzero.chatregulator.enums.InfractionType.*;

class InfractionPlayerTest {

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
