package net.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.Regulator;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;
import net.dreamerzero.chatregulator.config.MainConfig.CommandsConfig;
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
        Player infractor = infractorPlayer.getPlayer();
        if(infractor != null){
            switch(type){
                case REGULAR:
                    execute(infractor, infractorPlayer, config.getInfractionsConfig().getCommandsConfig(), type);
                    break;
                case FLOOD:
                    execute(infractor, infractorPlayer, config.getFloodConfig().getCommandsConfig(), type);
                    break;
                case SPAM:
                    execute(infractor, infractorPlayer, config.getSpamConfig().getCommandsConfig(), type);
                    break;
                case BCOMMAND:
                    execute(infractor, infractorPlayer, config.getCommandBlacklistConfig().getCommandsConfig(), type);
                    break;
                case UNICODE:
                    execute(infractor, infractorPlayer, config.getUnicodeConfig().getCommandsConfig(), type);
                    break;
                case CAPS:
                    execute(infractor, infractorPlayer, config.getCapsConfig().getCommandsConfig(), type);
                    break;
                case NONE: return;
            }
        }
    }

    private void execute(Player infractor, InfractionPlayer iPlayer, CommandsConfig config, InfractionType type){
        if(config.executeCommand() && iPlayer.getViolations(type) % config.violationsRequired() == 0){
            var currentServer = infractor.getCurrentServer();
            config.getCommandsToExecute().forEach(cmd -> {
                String commandToSend = cmd.replace("<player>", infractor.getUsername());
                if(currentServer.isPresent()){
                    commandToSend = commandToSend
                        .replace("<server>", currentServer.get().getServerInfo().getName());
                }
                final String cmdfinal = commandToSend;
                server.getCommandManager().executeAsync(server.getConsoleCommandSource(), cmdfinal).thenAcceptAsync(status -> {
                    if(!status.booleanValue()){
                        Regulator.getInstance().getLogger().warn("Error executing command {}", cmdfinal);
                    }
                });
            });
        }
    }

    /**
     * Check if the command provided is within the list of commands to be checked.
     * @param command the command executed
     * @return if the command is to be checked
     */
    public static boolean isCommand(String command){
        return Configuration.getBlacklist().getBlockedCommands().stream().anyMatch(command::contains);
    }
}
