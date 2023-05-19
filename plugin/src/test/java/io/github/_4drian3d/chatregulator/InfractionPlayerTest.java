package io.github._4drian3d.chatregulator;

import io.github._4drian3d.chatregulator.api.InfractionCount;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.github._4drian3d.chatregulator.api.enums.InfractionType.FLOOD;
import static io.github._4drian3d.chatregulator.api.enums.InfractionType.REGEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InfractionPlayerTest {

    @Test
    @DisplayName("Player Violation Count")
    void countTest(){
        InfractionPlayerImpl player = TestsUtils.dummyPlayer();
        InfractionCount count = player.getInfractions();

        count.addViolation(REGEX);
        count.addViolation(FLOOD);

        assertEquals(1, count.getCount(REGEX));
        assertEquals(1, count.getCount(FLOOD));

        count.resetViolations(REGEX, FLOOD);

        assertEquals(0, count.getCount(REGEX));
        assertEquals(0, count.getCount(FLOOD));

        assertTrue(player.isOnline());
    }
}
