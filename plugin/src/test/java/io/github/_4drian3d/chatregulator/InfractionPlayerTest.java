package io.github._4drian3d.chatregulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github._4drian3d.chatregulator.plugin.utils.TestsUtils;

import static io.github._4drian3d.chatregulator.api.enums.InfractionType.*;

class InfractionPlayerTest {

    @Test
    @DisplayName("Player Violation Count")
    void countTest(){
        InfractionPlayerImpl player = null;//InfractionPlayer.get(TestsUtils.createNormalPlayer("4drian3d"));
        InfractionCount count = player.getInfractions();

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
