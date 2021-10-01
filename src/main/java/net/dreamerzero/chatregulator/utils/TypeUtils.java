package net.dreamerzero.chatregulator.utils;

import java.util.List;

import net.dreamerzero.chatregulator.Regulator;

public class TypeUtils {
    public enum WarningType {
        TITLE(), ACTIONBAR(), MESSAGE();
    }

    public enum InfractionType{
        REGULAR(), FLOOD(), SPAM(), NONE();
    }

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
