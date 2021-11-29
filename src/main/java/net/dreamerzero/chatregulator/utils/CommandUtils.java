package net.dreamerzero.chatregulator.utils;

import java.util.Set;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

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
     * @param type the {@link InfractionType}
     * @param infractorPlayer the {@link InfractionPlayer} involved
     */
    public void executeCommand(InfractionType type, InfractionPlayer infractorPlayer){
        Player infractor = infractorPlayer.getPlayer().orElseThrow();
        //TODO: Simplify this, getCommandsConfig, executecommand and violationRequired are common
        switch(type){
            case REGULAR:
                var iconfig = config.getInfractionsConfig().getCommandsConfig();
                if(iconfig.executeCommand() && infractorPlayer.getViolations(type) % iconfig.violationsRequired() == 0){
                    iconfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case FLOOD:
                var fConfig = config.getFloodConfig().getCommandsConfig();
                if(fConfig.executeCommand() && infractorPlayer.getViolations(type) % fConfig.violationsRequired() == 0){
                    fConfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case SPAM:
                var sConfig = config.getSpamConfig().getCommandsConfig();
                if(sConfig.executeCommand() && infractorPlayer.getViolations(type) % sConfig.violationsRequired() == 0) {
                    sConfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case BCOMMAND:
                var cConfig = config.getCommandBlacklistConfig().getCommandsConfig();
                if(cConfig.executeCommand() && infractorPlayer.getViolations(type) % cConfig.violationsRequired() == 0){

                    cConfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
                }
                break;
            case UNICODE:
                var uConfig = config.getUnicodeConfig().getCommandsConfig();
                if(uConfig.executeCommand() && infractorPlayer.getViolations(type) % uConfig.violationsRequired() == 0){
                    uConfig.getCommandsToExecute().forEach(command -> execute(command, infractor));
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
