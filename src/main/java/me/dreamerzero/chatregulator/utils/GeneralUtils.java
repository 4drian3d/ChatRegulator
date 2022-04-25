package me.dreamerzero.chatregulator.utils;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.result.ReplaceableResult;
import me.dreamerzero.chatregulator.result.Result;
import me.dreamerzero.chatregulator.wrapper.event.EventWrapper;
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
     * @param iplayer the infraction player
     * @return if the player has flagged for spam
     */
    public static boolean spamCheck(Result result, InfractionPlayer iplayer){
        final MainConfig.Spam config = Configuration.getConfig().getSpamConfig();
        if(!result.isInfraction() || !config.getCooldownConfig().enabled()) {
            return false;
        }
        return iplayer.getTimeSinceLastMessage() < config.getCooldownConfig().unit().toMillis(config.getCooldownConfig().limit());
    }

    /**
     * Call violation event
     * @param bundle the event bundle
     * @param plugin chatregulator plugin
     * @return if the event is approved
     */
    public static boolean callViolationEvent(@NotNull EventBundle bundle, @NotNull ChatRegulator plugin) {
        return plugin.getProxy().getEventManager().fire(bundle.source() == SourceType.COMMAND
            ? new CommandViolationEvent(bundle.player(), bundle.type(), bundle.result, bundle.string)
            : new ChatViolationEvent(bundle.player(), bundle.type(), bundle.result, bundle.string))
            .thenApplyAsync(violationEvent -> {
                if(!violationEvent.getResult().isAllowed()) {
                    if(bundle.source() == SourceType.COMMAND)
                        bundle.player().lastCommand(bundle.string());
                    else
                        bundle.player().lastMessage(bundle.string());
                    return false;
                } else {
                    DebugUtils.debug(bundle.player, bundle.string, bundle.type(), bundle.result, plugin);
                    Statistics.getStatistics().addViolationCount(bundle.type());
                    ConfigManager.sendWarningMessage(bundle.player, bundle.result, bundle.type(), plugin.getFormatter());
                    ConfigManager.sendAlertMessage(bundle.player, bundle.type(), plugin);

                    bundle.player.getViolations().addViolation(bundle.type);
                    CommandUtils.executeCommand(bundle.type, bundle.player, plugin);
                    return true;
                }
        }).join();
    }

    /**
     * Call an event and check if it was not cancelled
     * @param bundle the event bundle
     * @param plugin chatregulator plugin
     * @return if the event was not cancelled
     */
    public static boolean checkAndCall(@NotNull EventBundle bundle, @NotNull ChatRegulator plugin) {
        return bundle.result().isInfraction() && GeneralUtils.callViolationEvent(bundle, plugin);
    }
    private GeneralUtils(){}

    public static boolean unicode(InfractionPlayer player, AtomicReference<String> string, EventWrapper<?> event, ChatRegulator plugin) {
        return GeneralUtils.allowedPlayer(player.getPlayer(), InfractionType.UNICODE)
            && UnicodeCheck.createCheck(string.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(new EventBundle(player, string.get(), InfractionType.UNICODE, result, event.source()), plugin)){
                    if(Configuration.getConfig().getUnicodeConfig().isBlockable()){
                        event.cancel();
                        event.resume();
                        return true;
                    }
                    if(result instanceof final ReplaceableResult replaceableResult){
                        string.set(replaceableResult.replaceInfraction());
                        event.setString(string.get());
                    }
                }
                return false;
            }).join();
    }

    public static boolean caps(InfractionPlayer player, AtomicReference<String> string, EventWrapper<?> event, ChatRegulator plugin) {
        return GeneralUtils.allowedPlayer(player.getPlayer(), InfractionType.CAPS)
            && CapsCheck.createCheck(string.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(new EventBundle(player, string.get(), InfractionType.CAPS, result, event.source()), plugin)){
                    if(Configuration.getConfig().getCapsConfig().isBlockable()){
                        event.cancel();
                        event.resume();
                        return true;
                    }
                    if(result instanceof final IReplaceable replaceable){
                        string.set(replaceable.replaceInfraction());
                        event.setString(string.get());
                    }
                }
                return false;
            }).join();
    }

    public static boolean flood(InfractionPlayer player, AtomicReference<String> string, EventWrapper<?> event, ChatRegulator plugin) {
        return GeneralUtils.allowedPlayer(player.getPlayer(), InfractionType.FLOOD)
            && FloodCheck.createCheck(string.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(new EventBundle(player, string.get(), InfractionType.FLOOD, result, event.source()), plugin)) {
                    if(Configuration.getConfig().getFloodConfig().isBlockable()){
                        event.cancel();
                        event.resume();
                        return true;
                    }
                    if(result instanceof final IReplaceable replaceable){
                        string.set(replaceable.replaceInfraction());
                        event.setString(string.get());
                    }
                }
                return false;
            }).join();
    }

    public static boolean regular(InfractionPlayer player, AtomicReference<String> string, EventWrapper<?> event, ChatRegulator plugin) {
        return GeneralUtils.allowedPlayer(player.getPlayer(), InfractionType.REGULAR)
            && InfractionCheck.createCheck(string.get()).thenApply(result -> {
                if(GeneralUtils.checkAndCall(new EventBundle(player, string.get(), InfractionType.REGULAR, result, event.source()), plugin)) {
                    if(Configuration.getConfig().getInfractionsConfig().isBlockable()){
                        event.cancel();
                        event.resume();
                        return true;
                    }
                    if(result instanceof final IReplaceable replaceable){
                        string.set(replaceable.replaceInfraction());
                        event.setString(string.get());
                    }
                }
                return false;
            }).join();
    }

    public static boolean spam(InfractionPlayer player, AtomicReference<String> string, EventWrapper<?> event, ChatRegulator plugin) {
        if(GeneralUtils.allowedPlayer(player.getPlayer(), InfractionType.SPAM)) {
            Result result = SpamCheck.createCheck(player, string.get(), event.source()).join();
            if(GeneralUtils.spamCheck(result, player)
                && GeneralUtils.callViolationEvent(new EventBundle(player, string.get(), InfractionType.SPAM, result, event.source()), plugin)
            ) {
                event.cancel();
                event.resume();
                return true;
            }
        }
        return false;
    }

    public static record EventBundle(InfractionPlayer player, String string, InfractionType type, Result result, SourceType source) {}
}
