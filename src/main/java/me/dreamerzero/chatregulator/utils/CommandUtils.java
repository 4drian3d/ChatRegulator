package me.dreamerzero.chatregulator.utils;

import java.util.Locale;
import java.util.Objects;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.config.Blacklist;
import me.dreamerzero.chatregulator.config.Configuration.CommandsConfig;
import me.dreamerzero.chatregulator.config.Configuration.Executable;
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
     * @param infractor the {@link InfractionPlayer} involved
     * @param plugin the plugin
     */
    public static void executeCommand(
        final @NotNull InfractionType type,
        final @NotNull InfractionPlayer infractor,
        final @NotNull ChatRegulator plugin
    ) {
        final Player player = Objects.requireNonNull(infractor).getPlayer();
        if(player == null){
            return;
        }

        final CommandsConfig config = ((Executable)type.getConfig(plugin.getConfig())).getCommandsConfig();
        if(config.executeCommand() && infractor.getViolations().getCount(type) % config.violationsRequired() == 0){
            final String servername = player.getCurrentServer().map(sv -> sv.getServerInfo().getName()).orElse("");
            config.getCommandsToExecute().forEach(cmd -> {
                final String command = cmd.replace("<player>", infractor.username())
                    .replace("<server>", servername);
                plugin.getProxy().getCommandManager()
                    .executeAsync(plugin.source(), command)
                    .handleAsync((status, ex) -> {
                        if (ex != null) {
                            plugin.getLogger().warn("Error executing command {}", command, ex);
                        } else if(!status.booleanValue()) {
                            plugin.getLogger().warn("Error executing command {}", command);
                        }
                        return null;
                    });
            });
        }
    }

    /**
     * Check if the command provided is within the list of commands to be checked.
     * @param command the command executed
     * @return if the command is to be checked
     */
    public static boolean isCommand(@NotNull String command, Blacklist blacklist) {
        final String firstArgument = getFirstArgument(Objects.requireNonNull(command));
        return blacklist.getBlockedCommands().stream()
            .anyMatch(firstArgument::equalsIgnoreCase);
    }

    /**
     * Get the first argument of a string
     * @param string the string
     * @return the first argument
     */
    public static @NotNull String getFirstArgument(final @NotNull String string) {
        final int index = Objects.requireNonNull(string).indexOf(" ");
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
                getLastChar(startingString) == '*'
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
