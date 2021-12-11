package me.dreamerzero.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig.CommandsConfig;
import me.dreamerzero.chatregulator.config.MainConfig.Executable;
import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Utilities for executing commands
 * when exceeding the set limit
 */
public class CommandUtils {
    private CommandUtils(){}
    /**
     * This will check if it is possible to execute the
     * configured commands when it detects that the limit
     * of a violation has been exceeded.
     * @param type the {@link InfractionType}
     * @param infractorPlayer the {@link InfractionPlayer} involved
     */
    public static void executeCommand(InfractionType type, InfractionPlayer infractorPlayer){
        Player infractor = infractorPlayer.getPlayer();
        if(infractor != null){
            execute(infractor, infractorPlayer, type);
        }
    }

    private static void execute(Player infractor, InfractionPlayer iPlayer, InfractionType type){
        CommandsConfig config = ((Executable)type.getConfig()).getCommandsConfig();
        if(config.executeCommand() && iPlayer.getViolations().getCount(type) % config.violationsRequired() == 0){
            var currentServer = infractor.getCurrentServer();
            config.getCommandsToExecute().forEach(cmd -> {
                String commandToSend = cmd.replace("<player>", infractor.getUsername());
                if(currentServer.isPresent()){
                    commandToSend = commandToSend
                        .replace("<server>", currentServer.get().getServerInfo().getName());
                }
                final String cmdfinal = commandToSend;
                ProxyServer proxy = ChatRegulator.getInstance().getProxy();
                proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), cmdfinal).thenAcceptAsync(status -> {
                    if(!status.booleanValue()){
                        ChatRegulator.getInstance().getLogger().warn("Error executing command {}", cmdfinal);
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

    /**
     * Get the first argument of a string
     * @param string the string
     * @return the first argument
     */
    public static String getFirstArgument(String string){
        int index = string.indexOf(" ");
        if (index == -1) {
            return string;
        }
        return string.substring(0, index);
    }
}
