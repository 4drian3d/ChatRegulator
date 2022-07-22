package me.dreamerzero.chatregulator.modules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.enums.InfractionType;

class StatisticTest {
    @Test
    @DisplayName("Global Statistics Test")
    void statisticTest(){
        Statistics stats = new Statistics();
        stats.addViolationCount(InfractionType.BCOMMAND);
        stats.addViolationCount(InfractionType.CAPS);

        assertEquals(1, stats.getViolationCount(InfractionType.BCOMMAND));
        assertEquals(2, stats.getViolationCount(InfractionType.NONE));
    }
}
