package net.dreamerzero.chatregulator;

import java.util.Optional;
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
    private int commandViolations;
    private boolean isOnline;
    private final String username;
    private long lastTimeSeen;

    /**
     * Constructor of an InfractorPlayer based on a {@link Player}
     * @param player the player on which it will be based
     */
    public InfractionPlayer(Player player){
        this.player = player;
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.commandViolations = 0;
        this.isOnline = true;
        this.username = player.getUsername();
    }

    /**
     * Constructor of an InfractorPlayer based on its {@link UUID}
     * @param uuid the uuid on which it will be based
     * @param server the proxy server
     */
    public InfractionPlayer(UUID uuid, ProxyServer server){
        this.player = server.getPlayer(uuid).get();
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.commandViolations = 0;
        this.isOnline = true;
        this.username = player.getUsername();
    }

    /**
     * A simple method to obtain the player's name
     * @return infraction player name
     */
    public String username(){
        return this.username;
    }

    /**
     * Returns the online status of the player
     * @return player online status
     */
    public boolean isOnline(){
        return this.isOnline;
    }

    /**
     * Sets the player's online status
     * @param status new online status
     */
    public void isOnline(boolean status){
        this.isOnline = status;
    }

    /**
     * Get the time in milliseconds
     * when the player was last seen.
     * @return time in microseconds of the
     * moment when the user exited
     */
    public long getLastSeen(){
        return this.lastTimeSeen;
    }

    /**
     * Sets the time at which the player has left the server
     */
    public void setLastSeen(){
        this.lastTimeSeen = System.currentTimeMillis();
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
            case BCOMMAND: commandViolations += 1; break;
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
            case BCOMMAND: commandViolations = newViolationsCount; break;
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
     * Get the amount of commands blocked the player already executed.
     * @return the commands blocked count executed by the player
     */
    public int getCommandInfractions(){
        return commandViolations;
    }

    /**
     * Obtain the original player
     * <p>
     * <strong>Check if the player is online with {@link #isOnline()}
     * if you are going to use this method
     * outside of a player event or command.</strong>
     * @return the original {@link Player}
     */
    public Optional<Player> getPlayer(){
        return Optional.of(this.player);
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link UUID}
     * @param uuid the player uuid
     * @param server the proxy server
     * @return the {@link InfractionPlayer}
     */
    public static Optional<InfractionPlayer> get(final UUID uuid, final ProxyServer server){
        if(Regulator.infractionPlayers.containsKey(uuid)){
            return Optional.of(Regulator.infractionPlayers.get(uuid));
        } else if(server.getPlayer(uuid).isPresent()) {
            InfractionPlayer infractionPlayer = new InfractionPlayer(uuid, server);
            Regulator.infractionPlayers.put(uuid, infractionPlayer);
            return Optional.of(infractionPlayer);
        } else {
            System.err.println("An attempt has been made to obtain a player who has not joined the server yet.");
            return Optional.of(null);
        }
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link Player}
     * @param player the player uuid
     * @return the {@link InfractionPlayer}
     */
    public static InfractionPlayer get(final Player player){
        final UUID uuid = player.getUniqueId();
        if(Regulator.infractionPlayers.containsKey(uuid)){
            return Regulator.infractionPlayers.get(uuid);
        } else {
            InfractionPlayer infractionPlayer = new InfractionPlayer(player);
            Regulator.infractionPlayers.put(uuid, infractionPlayer);
            return infractionPlayer;
        }
    }
}
