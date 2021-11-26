package net.dreamerzero.chatregulator.modules;

import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class Statistics {
    /**
     * Global Spam warning count
     */
    private static int spamCount;
    /**
     * Global Flood warning count
     */
    private static int floodCount;
    /**
     * Global Regular Infractions warning count
     */
    private static int regularCount;

    /**
     * Global commands blocked executed
     */
    private static int commandCount;

    /**
     * Global Unicode caracters count
     */
    private static int unicodeViolations;

    /**
     * Global Violations count
     */
    private static int globalViolations;

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public static void addViolationCount(InfractionType type){
        switch(type){
            case SPAM: spamCount++; break;
            case FLOOD: floodCount++; break;
            case REGULAR: regularCount++; break;
            case BCOMMAND: commandCount++; break;
            case UNICODE: unicodeViolations++; break;
            case NONE: break;
        }
        globalViolations++;
    }

    public static int getViolationCount(InfractionType type){
        switch(type){
            case SPAM: return spamCount;
            case FLOOD: return floodCount;
            case REGULAR: return regularCount;
            case BCOMMAND: return commandCount;
            case UNICODE: return unicodeViolations;
            case NONE: return globalViolations;
        }
        return 0;
    }

    private Statistics(){}
}
