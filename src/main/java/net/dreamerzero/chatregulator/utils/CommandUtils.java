package net.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;

/**
 * Utilities for executing commands
 * when exceeding the set limit
 */
public class CommandUtils {
    private ProxyServer server;
    private MainConfig.Config config;
    /**
     * CommandUtils constructor
     * @param server the proxy server
     * @param config the plugin config
     */
    public CommandUtils(ProxyServer server){
        this.server = server;
        this.config = Configuration.getConfig();
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
                var iconfig = config.getInfractionsConfig().getCommandsConfig();
                if(iconfig.executeCommand() &&
                    infractorPlayer.getRegularInfractions() % iconfig.violationsRequired() == 0){

                        iconfig.getCommandsToExecute().forEach(command -> {
                        String commandToSend = command
                            .replace("<player>", infractorPlayer.username())
                            .replace("<server>", infractor.getCurrentServer().get().getServerInfo().getName());
                        server.getCommandManager().executeAsync(server.getConsoleCommandSource(), commandToSend);
                    });
                }
                break;
            case FLOOD:
                var fconfig = config.getFloodConfig().getCommandsConfig();
                if(fconfig.executeCommand() &&
                    fconfig.violationsRequired() % infractorPlayer.getFloodInfractions() == 0){

                        fconfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case SPAM:
                var sconfig = config.getSpamConfig().getCommandsConfig();
                if(sconfig.executeCommand() && infractorPlayer.getSpamInfractions() % sconfig.violationsRequired() == 0) {

                    sconfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case BCOMMAND:
                var cconfig = config.getCommandBlacklistConfig().getCommandsConfig();
                if(cconfig.executeCommand() &&
                    infractorPlayer.getRegularInfractions() % cconfig.violationsRequired() == 0){

                    cconfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case UNICODE:
                var uconfig = config.getUnicodeConfig().getCommandsConfig();
                if(uconfig.executeCommand() &&
                    infractorPlayer.getUnicodeInfractions() % uconfig.violationsRequired() == 0){

                    uconfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
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
    public static boolean isCommand(String command){
        Set<String> commandsChecked = Configuration.getBlacklist().getBlockedCommands();

        return commandsChecked.stream().anyMatch(command::contains);
    }
}
