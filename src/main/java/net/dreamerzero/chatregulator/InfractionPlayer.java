package net.dreamerzero.chatregulator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.jetbrains.annotations.Nullable;

import net.dreamerzero.chatregulator.exception.PlayerNotAvailableException;

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
    private ViolationCount violationsCount;
    private boolean isOnline;
    private final String username;
    private Temporal lastTimeSeen;
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
        this.violationsCount = new ViolationCount();
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
        this.violationsCount = new ViolationCount();
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
    public InfractionPlayer isOnline(boolean status){
        this.isOnline = status;
        return this;
    }

    /**
     * Get the time in milliseconds
     * when the player was last seen.
     * @return time in microseconds of the
     * moment when the user exited
     */
    public long getLastSeen(){
        return Duration.between(this.lastTimeSeen, Instant.now()).toMillis();
    }

    /**
     * Sets the time at which the player has left the server
     */
    public InfractionPlayer setLastSeen(Temporal time){
        this.lastTimeSeen = time;
        return this;
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
    public InfractionPlayer lastMessage(String newLastMessage){
        this.preLastMessage = this.lastMessage;
        this.lastMessage = newLastMessage;
        this.timeSinceLastMessage = Instant.now();
        return this;
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
    public InfractionPlayer lastCommand(String newLastCommand){
        this.preLastCommand = this.lastCommand;
        this.lastCommand = newLastCommand;
        this.timeSinceLastCommand = Instant.now();
        return this;
    }

    /**
     * Get the time in milliseconds since the last message of the player
     * @return time in milliseconds since the last message
     */
    public long getTimeSinceLastMessage(){
        return Duration.between(this.timeSinceLastMessage, Instant.now()).toMillis();
    }

    /**
     * Get the time in milliseconds since the last command of the player
     * @return time in milliseconds since the last command
     */
    public long getTimeSinceLastCommand(){
        return Duration.between(this.timeSinceLastCommand, Instant.now()).toMillis();
    }

    /**
     * Get the violations count of the player
     * @return the violations count
     * @since 2.0.0
     */
    public ViolationCount getViolations(){
        return this.violationsCount;
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

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof InfractionPlayer)) return false;
        InfractionPlayer other = (InfractionPlayer)o;
        return other.getViolations().equals(this.getViolations()) || other.username.equals(this.username);
    }

    @Override
    public int hashCode(){
        if(this.player != null){
            return 31 + this.player.hashCode();
        }
        return 31 + this.username.hashCode();
    }

    @Override
    public String toString(){
        return "InfractionPlayer["
            +"name="+this.username
            +",online="+this.isOnline
            +",violationcount="+violationsCount.toString()
            +"]";
    }
}
