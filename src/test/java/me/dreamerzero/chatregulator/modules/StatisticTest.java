package me.dreamerzero.chatregulator.modules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.enums.InfractionType;

public class StatisticTest {
    @Test
    @DisplayName("Global Statistics Test")
    void statisticTest(){
        Statistics stats = Statistics.getStatistics();
        stats.addViolationCount(InfractionType.BCOMMAND);

        assertEquals(1, stats.getViolationCount(InfractionType.BCOMMAND));

        Statistics alternativeStats = Statistics.getStatistics();
        assertEquals(stats, alternativeStats);
    }
}
