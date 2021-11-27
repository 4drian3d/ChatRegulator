package net.dreamerzero.chatregulator.utils;

import java.util.List;

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
        Player infractor = infractorPlayer.getPlayer().orElseThrow();
        switch(type){
            case REGULAR:
                if(config.getBoolean("infractions.commands.execute-commands") &&
                    infractorPlayer.getRegularInfractions() % config.getInt("infractions.commands.violations-required") == 0){

                    config.getStringList("infractions.commands.commands-to-execute").forEach(command -> {
                        String commandToSend = command
                            .replace("<player>", infractorPlayer.username())
                            .replace("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                    });
                }
                break;
            case FLOOD:
                if(config.getBoolean("flood.commands.execute-commands") &&
                    config.getInt("flood.commands.violations-required") % infractorPlayer.getFloodInfractions() == 0){

                    config.getStringList("flood.commands.commands-to-execute").forEach(command -> execute(command, infractor));
                }
                break;
            case SPAM:
                if(config.getBoolean("spam.commands.execute-commands") &&
                    infractorPlayer.getSpamInfractions() % config.getInt("spam.commands.violations-required") == 0) {

                    config.getStringList("spam.commands.commands-to-execute").forEach(command -> execute(command, infractor));
                }
                break;
            case BCOMMAND:
                if(config.getBoolean("blocked-commands.commands.execute-commands") &&
                    infractorPlayer.getRegularInfractions() % config.getInt("blocked-commands.commands.violations-required") == 0){

                    config.getStringList("blocked-commands.commands-to-execute").forEach(command -> execute(command, infractor));
                }
                break;
            case UNICODE:
                if(config.getBoolean("unicode-blocker.commands.execute-commands") &&
                    infractorPlayer.getUnicodeInfractions() % config.getInt("unicode-blocker.commands.violations-required") == 0){

                    config.getStringList("unicode-blocker.commands-to-execute").forEach(command -> execute(command, infractor));
                }
                break;
            case NONE: return;
        }
    }

    private void execute(String command, Player infractor){
        String commandToSend = command.replace("<player>", infractor.getUsername());
        var currentServer = infractor.getCurrentServer();
        if(currentServer.isPresent()){
            commandToSend = commandToSend
                .replace("<server>", currentServer.get().getServerInfo().getName());
        }
        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
    }

    /**
     * Check if the command provided is within the list of commands to be checked.
     * @param command the command executed
     * @return if the command is to be checked
     */
    public static boolean isCommand(String command, Yaml config){
        List<String> commandsChecked = config.getStringList("commands-checked");

        return commandsChecked.stream().anyMatch(command::contains);
    }
}
