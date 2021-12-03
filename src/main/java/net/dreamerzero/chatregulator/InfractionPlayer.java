package net.dreamerzero.chatregulator;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.Nullable;

import net.dreamerzero.chatregulator.exception.PlayerNotAvailableException;
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
    private int unicodeViolations;
    private int capsviolations;
    private boolean isOnline;
    private final String username;
    private long lastTimeSeen;
    private Instant timeSinceLastMessage;
    private Instant timeSinceLastCommand;

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
        this.timeSinceLastMessage = Instant.now();
        this.timeSinceLastCommand = Instant.now();
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.commandViolations = 0;
        this.unicodeViolations = 0;
        this.capsviolations = 0;
        this.isOnline = true;
        this.username = player.getUsername();
    }

    /**
     * Constructor of an InfractorPlayer based on its {@link UUID}
     * @param uuid the uuid on which it will be based
     * @param server the proxy server
     */
    public InfractionPlayer(UUID uuid, ProxyServer server){
        this.player = server.getPlayer(uuid).orElseThrow();
        this.preLastMessage = " .";
        this.lastMessage = " ";
        this.preLastCommand = " ";
        this.lastCommand = " .";
        this.timeSinceLastMessage = Instant.now();
        this.timeSinceLastCommand = Instant.now();
        this.floodViolations = 0;
        this.regularViolations = 0;
        this.spamViolations = 0;
        this.commandViolations = 0;
        this.unicodeViolations = 0;
        this.capsviolations = 0;
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
        return this.lastMessage;
    }

    /**
     * Sets the player's last sent message
     * @param newLastMessage the new last message sent by the player
     */
    public void lastMessage(String newLastMessage){
        this.preLastMessage = this.lastMessage;
        this.lastMessage = newLastMessage;
        this.timeSinceLastMessage = Instant.now();
    }

    /**
     * Get the command prior to the player's last command
     * @return the command before the player's last command
     */
    public String preLastCommand(){
        return this.preLastCommand;
    }

    /**
     * Get the last command executed by the player
     * @return last command of the player
     */
    public String lastCommand(){
        return this.lastCommand;
    }

    /**
     * Sets the player's last executed command
     * @param newLastCommand the new last command executed by the player
     */
    public void lastCommand(String newLastCommand){
        this.preLastCommand = this.lastCommand;
        this.lastCommand = newLastCommand;
        this.timeSinceLastCommand = Instant.now();
    }

    public long getTimeSinceLastMessage(){
        return Duration.between(this.timeSinceLastMessage, Instant.now()).toMillis();
    }

    public long getTimeSinceLastCommand(){
        return Duration.between(this.timeSinceLastCommand, Instant.now()).toMillis();
    }

    /**
     * Adds an infraction to the count of any type of player infraction.
     * @param type the infraction type
     */
    public void addViolation(InfractionType type){
        switch(type){
            case SPAM: this.spamViolations++; break;
            case REGULAR: this.regularViolations++; break;
            case FLOOD: this.floodViolations++; break;
            case BCOMMAND: this.commandViolations++; break;
            case UNICODE: this.unicodeViolations++; break;
            case CAPS: this.capsviolations++; break;
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
            case SPAM: this.spamViolations = newViolationsCount; break;
            case REGULAR: this.regularViolations = newViolationsCount; break;
            case FLOOD: this.floodViolations = newViolationsCount; break;
            case BCOMMAND: this.commandViolations = newViolationsCount; break;
            case UNICODE: this.unicodeViolations = newViolationsCount; break;
            case CAPS: this.capsviolations = newViolationsCount; break;
            case NONE: return;
        }
    }

    /**
     * Reset the count of infraction of any type of this player
     * @param types the types
     */
    public void resetViolations(InfractionType... types){
        for(InfractionType type : types){
            this.setViolations(type, 0);
        }
    }

    /**
     * Get the ammount of violations of any type
     * @param type the violation type
     * @return the count
     */
    public int getViolations(InfractionType type){
        switch(type){
            case SPAM: return this.spamViolations;
            case REGULAR: return this.regularViolations;
            case FLOOD: return this.floodViolations;
            case BCOMMAND: return this.commandViolations;
            case UNICODE: return this.unicodeViolations;
            case CAPS: return this.capsviolations;
            case NONE: break;
        }
        return 0;
    }

    /**
     * Obtain the original player
     * <p>
     * <strong>Check if the player is online with {@link #isOnline()}
     * if you are going to use this method
     * outside of a player event or command.</strong>
     * @return the original {@link Player}
     */
    public @Nullable Player getPlayer(){
        return this.player;
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link UUID}
     * @param uuid the player uuid
     * @return the {@link InfractionPlayer}
     * @throws PlayerNotAvailableException if the player is not available
     */
    public static @Nullable InfractionPlayer get(final UUID uuid) throws PlayerNotAvailableException{
        if(Regulator.infractionPlayers.containsKey(uuid)){
            return Regulator.infractionPlayers.get(uuid);
        } else {
            Regulator plugin = Regulator.getInstance();
            Optional<Player> optionalPlayer = plugin.getProxy().getPlayer(uuid);
            if(optionalPlayer.isPresent()){
                InfractionPlayer iPlayer = InfractionPlayer.get(optionalPlayer.get());
                Regulator.infractionPlayers.put(uuid, iPlayer);
                return iPlayer;
            } else {
                throw new PlayerNotAvailableException(uuid);
            }
        }
    }

    /**
     * Get the {@link InfractionPlayer} based on a {@link Player}
     * @param player the player uuid
     * @return the {@link InfractionPlayer}
     */
    public static InfractionPlayer get(final Player player){
        final UUID uuid = player.getUniqueId();
        var playersMap = Regulator.infractionPlayers;
        if(playersMap.containsKey(uuid)){
            return playersMap.get(uuid);
        } else {
            InfractionPlayer infractionPlayer = new InfractionPlayer(player);
            playersMap.put(uuid, infractionPlayer);
            return infractionPlayer;
        }
    }
}
