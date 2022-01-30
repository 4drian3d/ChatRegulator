package me.dreamerzero.chatregulator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.exception.PlayerNotAvailableException;

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
    @Internal
    public InfractionPlayer(@NotNull Player player){
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
     * @throws PlayerNotAvailableException
     */
    @Internal
    InfractionPlayer(@NotNull UUID uuid) throws PlayerNotAvailableException{
        this.player = ChatRegulator.getInstance().getProxy().getPlayer(uuid).orElseThrow(PlayerNotAvailableException::new);
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
    public @NotNull String username(){
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
        return Duration.between(this.lastTimeSeen, Instant.now()).toMillis();
    }

    /**
     * Sets the time at which the player has left the server
     * @param time the time when the player was last seen
     */
    public void setLastSeen(Temporal time){
        this.lastTimeSeen = time;
    }

    /**
     * Get the message prior to the player's last message
     * @return the message before the player's last message
     */
    public @NotNull String preLastMessage(){
        return preLastMessage;
    }

    /**
     * Get the last message sent by the player
     * @return last message of the player
     */
    public @NotNull String lastMessage(){
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
    public @NotNull String preLastCommand(){
        return this.preLastCommand;
    }

    /**
     * Get the last command executed by the player
     * @return last command of the player
     */
    public @NotNull String lastCommand(){
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
    public @NotNull ViolationCount getViolations(){
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
        InfractionPlayer p = ChatRegulator.infractionPlayers.get(Objects.requireNonNull(uuid));
        if(p != null){
            return p;
        } else {
            ChatRegulator plugin = ChatRegulator.getInstance();
            Optional<Player> optionalPlayer = plugin.getProxy().getPlayer(uuid);
            if(optionalPlayer.isPresent()){
                InfractionPlayer iPlayer = InfractionPlayer.get(optionalPlayer.get());
                ChatRegulator.infractionPlayers.put(uuid, iPlayer);
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
    public static @NotNull InfractionPlayer get(@NotNull final Player player){
        final UUID uuid = Objects.requireNonNull(player).getUniqueId();
        var playersMap = ChatRegulator.infractionPlayers;
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
        return Objects.hash(this.player, this.username);
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
