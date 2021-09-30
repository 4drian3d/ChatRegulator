package net.dreamerzero.chatregulator.utils;

import java.util.List;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.minimessage.Template;

public class PlaceholderUtils {
    public static List<Template> getTemplates(Player player){
        return List.of(
            Template.of("player", player.getUsername()),
            Template.of("server", player.getCurrentServer().get().getServerInfo().getName()));
    }
}
