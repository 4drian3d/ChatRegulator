package net.dreamerzero.chatregulator.utils;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Utilities for bug or inconsistency resolution
 */
public class DebugUtils {

    /**
     * Debug message
     * @param player the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param check the check
     */
    public static void debug(InfractionPlayer player, String string, InfractionType detection, AbstractCheck check){
        final String pattern = check instanceof SpamCheck ? check.getInfractionWord() : check.getPattern();

        final Logger logger = Regulator.getInstance().getLogger();
        player.getPlayer().ifPresent(p -> logger.debug("User Detected: {}", p.getUsername()));
        logger.debug("Detection: {}", detection);
        logger.debug("String: {}", string);
        logger.debug("Pattern: {}", pattern);
    }

    private DebugUtils(){}
}
