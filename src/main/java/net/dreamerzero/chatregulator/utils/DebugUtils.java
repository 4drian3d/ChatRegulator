package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Utilities for bug or inconsistency resolution
 */
public class DebugUtils {

    /**
     * Debug message
     * @param infractor the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param check the check
     */
    public static void debug(InfractionPlayer infractor, String string, InfractionType detection, AbstractCheck check){
        final String pattern = check.getPattern();

        final Logger logger = Regulator.getInstance().getLogger();
        Player player = infractor.getPlayer();
        if(player != null) logger.debug("User Detected: {}", player.getUsername());
        logger.debug("Detection: {}", detection);
        logger.debug("String: {}", string);
        logger.debug("Pattern: {}", pattern);
    }

    private DebugUtils(){}
}
