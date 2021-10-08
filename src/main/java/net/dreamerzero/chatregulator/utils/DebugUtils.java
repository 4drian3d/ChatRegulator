package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Utilities for bug or inconsistency resolution
 */
public class DebugUtils {
    private Logger logger;
    private Yaml config;

    /**
     * Creates a debug object for easier troubleshooting
     * @param logger the logger
     * @param config the plugin config
     */
    public DebugUtils(Logger logger, Yaml config){
        this.logger = logger;
        this.config = config;
    }

    /**
     * Spam debug message
     * @param player the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     */
    public void debug(InfractionPlayer player, String string, InfractionType detection){
        String pattern = "Detected for spam in 3 detections";

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().get().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }

    /**
     * Regular Infraction debug message
     * @param player the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param iUtils the detection made
     */
    public void debug(InfractionPlayer player, String string, InfractionType detection, InfractionCheck iUtils){
        String pattern = iUtils.getPattern();

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().get().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }

    /**
     * Flood debug message
     * @param player the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param fUtils the detection made
     */
    public void debug(InfractionPlayer player, String string, InfractionType detection, FloodCheck fUtils){
        String pattern = fUtils.getPattern();

        if (config.getBoolean("debug")){
            logger.info("User Detected: {}", player.getPlayer().get().getUsername());
            logger.info("Detection: {}", detection.toString());
            logger.info("String: {}", string);
            logger.info("Pattern: {}", pattern);
        }
    }
}
