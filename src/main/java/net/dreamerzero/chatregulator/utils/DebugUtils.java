package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
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
     * Debug message
     * @param player the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param check the check
     */
    public void debug(InfractionPlayer player, String string, InfractionType detection, AbstractCheck check){
        final String pattern = check instanceof SpamCheck ? check.getInfractionWord() : check.getPattern();

        if (config.getBoolean("debug")){
            logger.debug("User Detected: {}", player.getPlayer().isPresent() ? player.getPlayer().get().getUsername() : "not present");
            logger.debug("Detection: {}", detection);
            logger.debug("String: {}", string);
            logger.debug("Pattern: {}", pattern);
        }
    }
}
