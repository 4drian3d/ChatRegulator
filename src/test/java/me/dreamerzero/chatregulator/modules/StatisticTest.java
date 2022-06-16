package me.dreamerzero.chatregulator.modules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.utils.TestsUtils;

public final class StatisticTest {
    @Test
    @DisplayName("Global Statistics Test")
    void statisticTest(){
        ChatRegulator plugin = TestsUtils.createRegulator();
        Statistics stats = plugin.getStatistics();
        stats.resetViolationCount();
        stats.addViolationCount(InfractionType.BCOMMAND);

        assertEquals(1, stats.getViolationCount(InfractionType.BCOMMAND));

        Statistics alternativeStats = plugin.getStatistics();
        assertEquals(stats, alternativeStats);
        stats.resetViolationCount();
    }
}
