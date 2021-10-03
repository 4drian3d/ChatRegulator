package net.dreamerzero.chatregulator.utils;

import java.util.List;

import de.leonhard.storage.Yaml;

public class TypeUtils {
    private Yaml config;
    public TypeUtils(Yaml config){
        this.config = config;
    }
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
    public boolean isCommand(String command){
        List<String> commandsChecked = config.getStringList("commands-checked");
        String commandParts[] = command.split(" ");

        for (String commandChecked : commandsChecked) {
            if (commandParts[0].contains(commandChecked)) {
                return true;
            }
        }
        return false;
    }
}
