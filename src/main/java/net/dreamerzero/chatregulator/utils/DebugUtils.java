package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class DebugUtils {
    private static Logger logger = LoggerFactory.getLogger(DebugUtils.class);
    public static void debug(InfractionPlayer player, String string, InfractionType detection){
        if (Regulator.getConfig().getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", detection == InfractionType.FLOOD ? FloodUtils.getFloodPattern() : InfractionUtils.getPattern(string));
        }
    }
}
