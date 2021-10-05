package net.dreamerzero.chatregulator;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * A player to whom the necessary warnings and variables can
 * be assigned in order to be sanctioned correctly.
 * To get the real player, check {@link #getPlayer()}
 */
public class InfractionPlayer {
    private final Player player;
    private String preLastMessage;
    private String lastMessage;
    private String preLastCommand;
    private String lastCommand;
    private int floodViolations;
    private int regularViolations;
    private int spamViolations;
    private boolean isOnline;
    private final String username;

    public InfractionPlayer(Player player){
        this.player = player;
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.isOnline = true;
        this.username = player.getUsername();
    }

    public InfractionPlayer(UUID uuid, ProxyServer server){
        this.player = server.getPlayer(uuid).get();
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.isOnline = true;
        this.username = player.getUsername();
    }

    /**
     * Returns the online status of the player
     * @return player online status
     */
    public boolean isOnline(){
        return this.isOnline;
    }

    /**
     * A simple method to obtain the player's name
     * @return infraction player name
     */
    public String username(){
        return this.username;
    }

    /**
     * Sets the player's online status
     * @param status new online status
     */
    public void isOnline(boolean status){
        this.isOnline = status;
    }

    /**
     * Get the message prior to the player's last message
     * @return the message before the player's last message
     */
    public String preLastMessage(){
        return preLastMessage;
    }

    /**
     * Get the last message sent by the player
     * @return last message of the player
     */
    public String lastMessage(){
        return lastMessage;
    }

    /**
     * Sets the player's last sent message
     * @param newLastMessage the new last message sent by the player
     */
    public void lastMessage(String newLastMessage){
        preLastMessage = lastMessage;
        lastMessage = newLastMessage;
    }

    /**
     * Get the command prior to the player's last command
     * @return the command before the player's last command
     */
    public String preLastCommand(){
        return preLastCommand;
    }

    /**
     * Get the last command executed by the player
     * @return last command of the player
     */
    public String lastCommand(){
        return lastCommand;
    }

    /**
     * Sets the player's last executed command
     * @param newLastCommand the new last command executed by the player
     */
    public void lastCommand(String newLastCommand){
        preLastCommand = lastCommand;
        lastCommand = newLastCommand;
    }

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(InfractionType type){
        switch(type){
            case SPAM: spamViolations += 1; break;
            case REGULAR: regularViolations += 1; break;
            case FLOOD: floodViolations += 1; break;
            case NONE: return;
        }
    }

    /**
     * Sets the new number of infractions of some kind that the player will have.
     * @param type the type of infraction
     * @param newViolationsCount the new number of infractions
     */
    public void setViolations(InfractionType type, int newViolationsCount){
        switch(type){
            case SPAM: spamViolations = newViolationsCount; break;
            case REGULAR: regularViolations = newViolationsCount; break;
            case FLOOD: floodViolations = newViolationsCount; break;
            case NONE: return;
        }
    }

    /**
     * Get the amount of flood infractions the player already has.
     * @return the flood infractions of the player
     */
    public int getFloodInfractions(){
        return floodViolations;
    }

    /**
     * Get the amount of regular infractions the player already has.
     * @return the regular infractions of the player
     */
    public int getRegularInfractions(){
        return regularViolations;
    }

    /**
     * Get the amount of spam infractions the player already has.
     * @return the spam infractions of the player
     */
    public int getSpamInfractions(){
        return spamViolations;
    }

    /**
     * Obtain the original player
     * @return the original {@link Player}
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link UUID}
     * @param uuid the player uuid
     * @return the {@link InfractionPlayer}
     */
    public static InfractionPlayer get(UUID uuid, ProxyServer server){
        if(Regulator.infractionPlayers.containsKey(uuid)){
            return Regulator.infractionPlayers.get(uuid);
        } else {
            InfractionPlayer infractionPlayer = new InfractionPlayer(uuid, server);
            Regulator.infractionPlayers.put(uuid, infractionPlayer);
            return infractionPlayer;
        }
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link Player}
     * @param player the player uuid
     * @return the {@link InfractionPlayer}
     */
    public static InfractionPlayer get(Player player){
        UUID uuid = player.getUniqueId();
        if(Regulator.infractionPlayers.containsKey(uuid)){
            return Regulator.infractionPlayers.get(uuid);
        } else {
            InfractionPlayer infractionPlayer = new InfractionPlayer(player);
            Regulator.infractionPlayers.put(uuid, infractionPlayer);
            return infractionPlayer;
        }
    }
}
