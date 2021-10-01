package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.Regulator;

public class CommandUtils {
    public static void executeCommand(TypeUtils.InfractionType type, Player infractor){
        ProxyServer server = Regulator.getProxyServer();
        switch(type){
            case REGULAR: if(Regulator.getConfig().getBoolean("infractions.execute-commands")){
                Regulator.getConfig().getStringList("infractions.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case FLOOD: if(Regulator.getConfig().getBoolean("flood.execute-commands")){
                Regulator.getConfig().getStringList("flood.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case NONE: return;
        }
    }
}
