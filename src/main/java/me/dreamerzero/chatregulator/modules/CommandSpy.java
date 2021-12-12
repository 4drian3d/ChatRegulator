package me.dreamerzero.chatregulator.modules;

import com.velocitypowered.api.command.CommandSource;

import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.utils.CommandUtils;

public class CommandSpy {
    private CommandSpy(){}

    public static boolean shouldAnnounce(CommandSource source, String command, MainConfig.CommandSpy config){
        return config.ignoredCommands().contains(CommandUtils.getFirstArgument(command))
            && source.hasPermission("chatregulator.bypass.commandspy");
    }
}
