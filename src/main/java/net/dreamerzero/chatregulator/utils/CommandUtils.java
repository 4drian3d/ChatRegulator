package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;

public class CommandUtils {
    private ProxyServer server;
    private Yaml config;
    public CommandUtils(ProxyServer server, Yaml config){
        this.server = server;
        this.config = config;
    }
    public void executeCommand(TypeUtils.InfractionType type, Player infractor){
        switch(type){
            case REGULAR: if(config.getBoolean("infractions.commands.execute-commands")){
                config.getStringList("infractions.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case FLOOD: if(config.getBoolean("flood.commands.execute-commands")){
                config.getStringList("flood.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case SPAM: if(config.getBoolean("spam.commands.execute-commands")) {
                config.getStringList("spam.commands.commands-to-execute").forEach(command -> {
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
