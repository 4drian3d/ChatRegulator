package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.kyori.adventure.text.Component;

public final class GeneralUtils {

    public static boolean allowedPlayer(Player player, InfractionType type){
        return type.getConfig().enabled() && !player.hasPermission(type.bypassPermission());
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
