package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.FloodCheck;
import net.dreamerzero.chatregulator.modules.InfractionCheck;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class DebugUtils {
    private Logger logger;
    private Yaml config;

    public DebugUtils(Logger logger, Yaml config){
        this.logger = logger;
        this.config = config;
    }

    public void debug(InfractionPlayer player, String string, InfractionType detection){
        String pattern = "Detected for 3 detections";

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }

    public void debug(InfractionPlayer player, String string, InfractionType detection, InfractionCheck iUtils){
        String pattern = iUtils.getPattern();

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }

    public void debug(InfractionPlayer player, String string, InfractionType detection, FloodCheck fUtils){
        String pattern = fUtils.getFloodPattern();

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }
}
