package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.modules.FloodUtils;
import net.dreamerzero.chatregulator.modules.InfractionUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class DebugUtils {
    private Logger logger;
    private FloodUtils fUtils;
    private InfractionUtils iUtils;
    private Yaml config;

    public DebugUtils(FloodUtils fUtils, InfractionUtils iUtils, Logger logger, Yaml config){
        this.fUtils = fUtils;
        this.iUtils = iUtils;
        this.logger = logger;
        this.config = config;
    }

    public void debug(InfractionPlayer player, String string, InfractionType detection){
        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", detection == InfractionType.FLOOD ? fUtils.getFloodPattern() : iUtils.getPattern(string));
        }
    }
}
