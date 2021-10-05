package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;

/**
 * Utilities for executing commands
 * when exceeding the set limit
 */
public class CommandUtils {
    private ProxyServer server;
    private Yaml config;
    /**
     * CommandUtils constructor
     * @param server the proxy server
     * @param config the plugin config
     */
    public CommandUtils(ProxyServer server, Yaml config){
        this.server = server;
        this.config = config;
    }

    /**
     * This will check if it is possible to execute the
     * configured commands when it detects that the limit
     * of a violation has been exceeded.
     * @param type the {@link TypeUtils.InfractionType}
     * @param infractorPlayer the {@link InfractionPlayer} involved
     */
    public void executeCommand(TypeUtils.InfractionType type, InfractionPlayer infractorPlayer){
        Player infractor = infractorPlayer.getPlayer();
        switch(type){
            case REGULAR: if(config.getBoolean("infractions.commands.execute-commands") &&
            infractorPlayer.getRegularInfractions() % config.getInt("infractions.commands.violations-required") == 0){

                config.getStringList("infractions.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractorPlayer.username())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case FLOOD: if(config.getBoolean("flood.commands.execute-commands") &&
                config.getInt("flood.commands.violations-required") % infractorPlayer.getFloodInfractions() == 0){

                config.getStringList("flood.commands.commands-to-execute").forEach(command -> {
                    String commandToSend = command
                        .replaceAll("<player>", infractor.getUsername())
                        .replaceAll("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                    server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                });
                break;
            }
            case SPAM: if(config.getBoolean("spam.commands.execute-commands") &&
                infractorPlayer.getSpamInfractions() % config.getInt("spam.commands.violations-required") == 0) {

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
