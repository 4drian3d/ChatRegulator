package me.dreamerzero.chatregulator.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import me.dreamerzero.chatregulator.enums.Permissions;

public class TestsUtils {
    public static Player createNormalPlayer(String name){
        Player player = mock(Player.class);

        when(player.getUsername()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getClientBrand()).thenReturn("vanilla");
        when(player.hasPermission(Permissions.BYPASS_INFRACTIONS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_FLOOD)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_CAPS)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_SPAM)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_UNICODE)).thenReturn(false);
        when(player.hasPermission(Permissions.BYPASS_COMMANDSPY)).thenReturn(false);

        return player;
    }

    public static Player createOperatorPlayer(String name){
        Player player = mock(Player.class);

        when(player.getUsername()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getClientBrand()).thenReturn("vanilla");
        when(player.hasPermission(Permissions.BYPASS_INFRACTIONS)).thenReturn(true);
        when(player.hasPermission(Permissions.BYPASS_FLOOD)).thenReturn(true);
        when(player.hasPermission(Permissions.BYPASS_CAPS)).thenReturn(true);
        when(player.hasPermission(Permissions.BYPASS_SPAM)).thenReturn(true);
        when(player.hasPermission(Permissions.BYPASS_UNICODE)).thenReturn(true);
        when(player.hasPermission(Permissions.BYPASS_COMMANDSPY)).thenReturn(true);
        when(player.hasPermission(Permissions.NOTIFICATIONS)).thenReturn(true);

        return player;
    }
}
