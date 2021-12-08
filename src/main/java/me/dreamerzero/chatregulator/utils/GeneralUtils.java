package me.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.enums.InfractionType;
import net.kyori.adventure.text.Component;

public final class GeneralUtils {

    /**
     * Check if the player can be checked
     * @param player the infraction player
     * @param type the infractiontype
     * @return if the player can be checked
     */
    public static boolean allowedPlayer(Player player, InfractionType type){
        return type.getConfig().enabled() && !player.hasPermission(type.bypassPermission());
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
