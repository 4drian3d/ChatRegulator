package me.dreamerzero.chatregulator.utils;

import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.events.ChatViolationEvent;
import me.dreamerzero.chatregulator.events.CommandViolationEvent;

/**
 * General utils
 */
public final class GeneralUtils {

    /**
     * Check if the player can be checked
     * @param player the infraction player
     * @param type the infraction type
     * @return if the player can be checked
     */
    public static boolean allowedPlayer(@NotNull Player player, InfractionType type){
        return type.getConfig().get().enabled() && !Objects.requireNonNull(player).hasPermission(type.bypassPermission());
    }

    /**
     * Check if a player has spammed
     * @param result the result
     * @param config the config
     * @param iplayer the infraction player
     * @return if the player has flagged for spam
     */
    public static boolean spamCheck(Result result, MainConfig.Config config, InfractionPlayer iplayer){
        MainConfig.Spam sconfig = config.getSpamConfig();
        return result.isInfraction()
            && (sconfig.getCooldownConfig().enabled() && iplayer.getTimeSinceLastMessage() < sconfig.getCooldownConfig().limit()
                || !sconfig.getCooldownConfig().enabled());
    }

    /**
     * Call violation event
     * @param player detected player
     * @param string the string of the event
     * @param infractionType the infraction type
     * @param result the result
     * @param stype the source type
     * @return if the event is approved
     */
    public static boolean callViolationEvent(
        @NotNull InfractionPlayer player,
        @NotNull String string,
        @NotNull InfractionType infractionType,
        @NotNull Result result,
        @NotNull SourceType stype,
        @NotNull ChatRegulator plugin) {

        return plugin.getProxy().getEventManager().fire(stype == SourceType.COMMAND
            ? new CommandViolationEvent(player, infractionType, result, string)
            : new ChatViolationEvent(player, infractionType, result, string))
            .thenApplyAsync(violationEvent -> {
                if(!violationEvent.getResult().isAllowed()) {
                    if(stype == SourceType.COMMAND) player.lastCommand(string); else player.lastMessage(string);
                    return false;
                } else {
                    DebugUtils.debug(player, string, infractionType, result, plugin);
                    Statistics.getStatistics().addViolationCount(infractionType);
                    ConfigManager.sendWarningMessage(player, result, infractionType);
                    ConfigManager.sendAlertMessage(player, infractionType, plugin);

                    player.getViolations().addViolation(infractionType);
                    CommandUtils.executeCommand(infractionType, player, plugin);
                    return true;
                }
        }).join();
    }

    /**
     * Call an event and check if it was not cancelled
     * @param player the {@link InfractionPlayer}
     * @param string the string of the event (Command/Chat Message executed)
     * @param type the infraction type
     * @param result the result of the detection
     * @param stype the source type
     * @return if the event was not cancelled
     */
    public static boolean checkAndCall(
        @NotNull InfractionPlayer player,
        @NotNull String string,
        @NotNull InfractionType type,
        @NotNull Result result,
        @NotNull SourceType stype,
        @NotNull ChatRegulator plugin){
        return result.isInfraction() && GeneralUtils.callViolationEvent(player, string, type, result, stype, plugin);
    }
    private GeneralUtils(){}
}
