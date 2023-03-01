package io.github._4drian3d.chatregulator.plugin.modules;

import com.velocitypowered.api.command.CommandSource;

import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.api.enums.Permission;

/**
 * Command Spy utils
 */
public final class CommandSpy {
    private CommandSpy(){}

    /**
     * Check if we should announce the command to operators
     * @param source the source of the command
     * @param command the command
     * @param config the command spy config
     * @return if we should announce
     */
    public static boolean shouldAnnounce(CommandSource source, String command, Configuration.CommandSpy config){
        return config.ignoredCommands().contains(Commands.getFirstArgument(command))
            && Permission.BYPASS_COMMAND_SPY.test(source);
    }
}
