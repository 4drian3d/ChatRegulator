package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;

public class CommandUtils {
    private ProxyServer server;
    private Yaml config;
    public CommandUtils(ProxyServer server, Yaml config){
        this.server = server;
        this.config = config;
    }
    public void executeCommand(TypeUtils.InfractionType type, InfractionPlayer infractorPlayer){
        Player infractor = infractorPlayer.getPlayer();
        //TODO: Debug
        System.out.println("infracciones spam: " + infractorPlayer.getSpamInfractions());
        System.out.println("infracciones max config: " + config.getInt("spam.commands.violations-required"));
        switch(type){
            case REGULAR: if(config.getBoolean("infractions.commands.execute-commands") &&
            infractorPlayer.getRegularInfractions() >= config.getInt("infractions.commands.violations-required")){

                config.getStringList("infractions.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractorPlayer.username())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case FLOOD: if(config.getBoolean("flood.commands.execute-commands") &&
                config.getInt("flood.commands.violations-required") <= infractorPlayer.getFloodInfractions()){

                config.getStringList("flood.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case SPAM: if(config.getBoolean("spam.commands.execute-commands") &&
                infractorPlayer.getSpamInfractions() >= config.getInt("spam.commands.violations-required")) {

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
