package me.dreamerzero.chatregulator.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.events.ChatViolationEvent;
import me.dreamerzero.chatregulator.events.CommandViolationEvent;
import net.kyori.adventure.text.Component;

/**
 * General utils
 */
public final class GeneralUtils {

    /**
     * Check if the player can be checked
     * @param player the infraction player
     * @param type the infractiontype
     * @return if the player can be checked
     */
    public static boolean allowedPlayer(@NotNull Player player, @NotNull InfractionType type){
        return type.getConfig().enabled() && !Objects.requireNonNull(player).hasPermission(type.bypassPermission());
    }

    /**
     * Check if a player has spammed
     * @param check the check
     * @param config the config
     * @param iplayer the infraction player
     * @return if the player has flagged for spam
     */
    public static boolean spamCheck(SpamCheck check, MainConfig.Config config, InfractionPlayer iplayer){
        MainConfig.Spam sconfig = config.getSpamConfig();
        return check.isInfraction()
            && (sconfig.getCooldownConfig().enabled() && iplayer.getTimeSinceLastMessage() < sconfig.getCooldownConfig().limit()
                || !sconfig.getCooldownConfig().enabled());
    }

    /**
     * Call violation event
     * @param player detected player
     * @param string the string of the event
     * @param detection the detection
     * @param stype the source type
     * @return if the event is approved
     */
    public static boolean callViolationEvent(@NotNull InfractionPlayer player, @NotNull String string, @NotNull AbstractCheck detection, @NotNull SourceType stype) {
        AtomicBoolean approved = new AtomicBoolean(true);
        InfractionType type = detection.type();
        ChatRegulator.getInstance().getProxy().getEventManager().fire(stype == SourceType.COMMAND
            ? new CommandViolationEvent(player, type, detection, string)
            : new ChatViolationEvent(player, type, detection, string))
            .thenAcceptAsync(violationEvent -> {
                if(!violationEvent.getResult().isAllowed()) {
                    approved.set(false);
                    if(stype == SourceType.COMMAND) player.lastCommand(string); else player.lastMessage(string);
                } else {
                    DebugUtils.debug(player, string, type, detection);
                    Statistics.getStatistics().addViolationCount(type);
                    ConfigManager.sendWarningMessage(player, type, detection);
                    ConfigManager.sendAlertMessage(player, type);

                    player.getViolations().addViolation(type);
                    CommandUtils.executeCommand(type, player);
                }
        });
        return approved.get();
    }

    /**
     * Call an event and check if it was not cancelled
     * @param player the {@link InfractionPlayer}
     * @param string the string of the event (Command/Chat Message executed)
     * @param detection the detection
     * @param stype the source type
     * @return if the event was not cancelled
     */
    public static boolean checkAndCall(@NotNull InfractionPlayer player, @NotNull String string, @NotNull AbstractCheck detection, @NotNull SourceType stype){
        return detection.isInfraction() && GeneralUtils.callViolationEvent(player, string, detection, stype);
    }

    /**
     * Spaces component for "/chatregulator clear" command
     */
    public static final Component spacesComponent = Component.text()
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .append(Component.newline())
        .build();
    private GeneralUtils(){}
}
