package me.dreamerzero.chatregulator.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.config.Messages;
import me.dreamerzero.chatregulator.config.MainConfig.Executable;
import me.dreamerzero.chatregulator.config.MainConfig.Toggleable;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.result.Result;
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
     * @param toggleable the toggleable config
     * @param type the infraction type
     * @return if the player can be checked
     */
    public static boolean allowedPlayer(@NotNull Player player, @NotNull Toggleable toggleable, InfractionType type){
        return toggleable.enabled() && !Objects.requireNonNull(player).hasPermission(type.bypassPermission());
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
        MainConfig.Warning config,
        Messages.Warning messages) {
        AtomicBoolean approved = new AtomicBoolean(true);
        ChatRegulator.getInstance().getProxy().getEventManager().fire(stype == SourceType.COMMAND
            ? new CommandViolationEvent(player, infractionType, result, string)
            : new ChatViolationEvent(player, infractionType, result, string))
            .thenAcceptAsync(violationEvent -> {
                if(!violationEvent.getResult().isAllowed()) {
                    approved.set(false);
                    if(stype == SourceType.COMMAND) player.lastCommand(string); else player.lastMessage(string);
                } else {
                    DebugUtils.debug(player, string, infractionType, result);
                    Statistics.getStatistics().addViolationCount(infractionType);
                    ConfigManager.sendWarningMessage(player, result, messages, config);
                    ConfigManager.sendAlertMessage(player, infractionType);

                    player.getViolations().addViolation(infractionType);
                    CommandUtils.executeCommand(infractionType, player, (Executable)config);
                }
        });
        return approved.get();
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
        MainConfig.Warning config,
        Messages.Warning messages){
        return result.isInfraction() && GeneralUtils.callViolationEvent(player, string, type, result, stype, config, messages);
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
