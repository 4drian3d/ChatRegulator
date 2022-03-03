package me.dreamerzero.chatregulator.utils;

import java.util.Locale;
import java.util.Objects;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.NotNull;

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
public final class CommandUtils {
    private CommandUtils(){}
    /**
     * This will check if it is possible to execute the
     * configured commands when it detects that the limit
     * of a violation has been exceeded.
     * @param type the {@link InfractionType}
     * @param infractorPlayer the {@link InfractionPlayer} involved
     * @param config the executable config
     */
    public static void executeCommand(@NotNull InfractionType type, @NotNull InfractionPlayer infractorPlayer, Executable config){
        Player infractor = Objects.requireNonNull(infractorPlayer).getPlayer();
        if(infractor != null){
            execute(infractor, infractorPlayer, type, config);
        }
    }

    private static void execute(@NotNull Player infractor, @NotNull InfractionPlayer iPlayer, @NotNull InfractionType type, Executable executable){
        CommandsConfig config = executable.getCommandsConfig();
        if(config.executeCommand() && iPlayer.getViolations().getCount(type) % config.violationsRequired() == 0){
            final String servername = infractor.getCurrentServer().map(sv -> sv.getServerInfo().getName()).orElse("");
            config.getCommandsToExecute().forEach(cmd -> {
                final String commandToSend = cmd.replace("<player>", infractor.getUsername()).replace("<server>", servername);
                final ProxyServer proxy = ChatRegulator.getInstance().getProxy();
                proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), commandToSend).thenAcceptAsync(status -> {
                    if(!status.booleanValue()){
                        ChatRegulator.getInstance().getLogger().warn("Error executing command {}", commandToSend);
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
    public static boolean isCommand(@NotNull String command){
        final String firstArgument = getFirstArgument(Objects.requireNonNull(command));
        return Configuration.getBlacklist().getBlockedCommands().stream()
            .anyMatch(firstArgument::equalsIgnoreCase);
    }

    /**
     * Get the first argument of a string
     * @param string the string
     * @return the first argument
     */
    public static @NotNull String getFirstArgument(@NotNull String string){
        int index = Objects.requireNonNull(string).indexOf(" ");
        if (index == -1) {
            return string;
        }
        return string.substring(0, index);
    }

    /**
     * Check if a string starts with another string, checking for its arguments
     * If the second string has a "*" symbol at the end,
     * it will be checked by means of a {@link String#startsWith(String)}
     * @param string the base string
     * @param startingString the starting string
     * @return if a string starts with another string
     */
    public static boolean isStartingString(@NotNull String string, @NotNull String startingString){
        if(Objects.requireNonNull(string).length() < Objects.requireNonNull(startingString).length()){
            return false;
        }
        startingString = startingString.toLowerCase(Locale.ROOT);
        string = string.toLowerCase(Locale.ROOT);
        return string.equals(startingString) ||
            string.startsWith(
                CommandUtils.getLastChar(startingString) == '*'
                    ? startingString.substring(0, startingString.length()-1)
                    : startingString.concat(" ")
            );
    }

    /**
     * Get the last character of a string
     * @param string the string
     * @return the last character
     */
    public static char getLastChar(@NotNull String string){
        return string.charAt(string.length()-1);
    }
}
