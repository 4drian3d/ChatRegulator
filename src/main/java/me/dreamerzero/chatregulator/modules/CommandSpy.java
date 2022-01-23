package me.dreamerzero.chatregulator.modules;

import com.velocitypowered.api.command.CommandSource;

import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.enums.Permissions;
import me.dreamerzero.chatregulator.utils.CommandUtils;

public class CommandSpy {
    private CommandSpy(){}

    /**
     * Check if we should announce the command to operators
     * @param source the source of the command
     * @param command the command
     * @param config the command spy config
     * @return if we should announce
     */
    public static boolean shouldAnnounce(CommandSource source, String command, MainConfig.CommandSpy config){
        return config.ignoredCommands().contains(CommandUtils.getFirstArgument(command))
            && source.hasPermission(Permissions.BYPASS_COMMANDSPY);
    }
}
