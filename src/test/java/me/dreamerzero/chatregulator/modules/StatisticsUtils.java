package me.dreamerzero.chatregulator.modules;

import me.dreamerzero.chatregulator.enums.InfractionType;

public final class StatisticsUtils {
    public static void resetStatistics(){
        Statistics.getStatistics().resetViolationCount();
    }

    public static void setStatistics(InfractionType type, int amount){
        Statistics.getStatistics().setViolationCount(type, amount);
    }
}
