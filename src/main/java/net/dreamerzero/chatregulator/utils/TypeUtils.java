package net.dreamerzero.chatregulator.utils;

import java.util.List;

import net.dreamerzero.chatregulator.Regulator;

public class TypeUtils {
    /**
     * The warning format to be executed
     */
    public enum WarningType {
        TITLE(), ACTIONBAR(), MESSAGE();
    }

    /**
     * The type of violation detected
     */
    public enum InfractionType{
        REGULAR(), FLOOD(), SPAM(), NONE();
    }

    /**
     * The type of detection the infraction has been detected
     */
    public enum SourceType{
        COMMAND(), CHAT();
    }

    /**
     * Check if the command provided is within the list of commands to be checked.
     * @param command the command executed
     * @return if the command is to be checked
     */
    public static boolean isCommand(String command){
        List<String> commandsChecked = Regulator.getConfig().getStringList("commands-checked");
        String commandParts[] = command.split(" ");

        for (String commandChecked : commandsChecked) {
            if (commandParts[0].contains(commandChecked)) {
                return true;
            }
        }
        return false;
    }
}
