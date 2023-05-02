package io.github._4drian3d.chatregulator.modules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github._4drian3d.chatregulator.plugin.StatisticsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;

class StatisticTest {
    @Test
    @DisplayName("Global StatisticsImpl Test")
    void statisticTest(){
        StatisticsImpl stats = new StatisticsImpl();
        stats.addInfractionCount(InfractionType.BLOCKED_COMMAND);
        stats.addInfractionCount(InfractionType.CAPS);

        assertEquals(1, stats.getInfractionCount(InfractionType.BLOCKED_COMMAND));
        assertEquals(2, stats.getInfractionCount(InfractionType.GLOBAL));
    }
}
