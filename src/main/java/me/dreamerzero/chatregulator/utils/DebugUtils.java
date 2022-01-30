package me.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;

import org.slf4j.Logger;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.result.PatternResult;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Utilities for bug or inconsistency resolution
 */
public final class DebugUtils {

    /**
     * Debug message
     * @param infractor the {@link InfractionPlayer} involved
     * @param string the message/command
     * @param detection the detection type
     * @param result the result
     */
    public static void debug(InfractionPlayer infractor, String string, InfractionType detection, Result result){

        final Logger logger = ChatRegulator.getInstance().getLogger();
        if(!logger.isDebugEnabled()) return;
        Player player = infractor.getPlayer();
        if(player != null) logger.debug("User Detected: {}", player.getUsername());
        logger.debug("Detection: {}", detection);
        logger.debug("String: {}", string);
        if(result instanceof PatternResult){
            var pattern = ((PatternResult)result).getPattern();
            if(pattern != null)
                logger.debug("Pattern: {}", pattern.pattern());
        }
    }

    private DebugUtils(){}
}
