package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.modules.checks.Check;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

/**
 * Basis for infringement events
 */
public class ViolationEvent implements ResultedEvent<GenericResult> {
    /**
     * InfractionPlayer involved in detection
     */
    protected final InfractionPlayer infractionPlayer;
    /**
     * Type of detection
     */
    protected final InfractionType type;
    /**
     * Global Spam warning count
     */
    public static int spamCount,
    /**
     * Global Flood warning count
     */
    floodCount,
    /**
     * Global Regular Infractions warning count
     */
    regularCount,

    /**
     * Global commands blocked executed
     */
    commandCount,

    /**
     * Global Unicode caracters count
     */
    unicodeViolations;
    private Check detection;
    private GenericResult result = GenericResult.allowed();

    /**
     * ViolationEvent Contructor
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     */
    public ViolationEvent(InfractionPlayer infractionPlayer, InfractionType type, Check detection){
        this.infractionPlayer = infractionPlayer;
        this.type = type;
        this.detection = detection;
    }

    /**
     * Get the InfractorPlayer that has committed the infraction
     * To get the original player, check {@link InfractionPlayer#getPlayer()}
     * @return the infractor
     * @since 1.1.0
     */
    public InfractionPlayer getInfractionPlayer(){
        return this.infractionPlayer;
    }

    /**
     * Get the type of infraction committed
     * @return the infraction committed
     * @since 1.1.0
     */
    public InfractionType getType(){
        return this.type;
    }

    /**
     * Obtain the detection performed
     * With this object, you can get the pattern,
     * the detected string and more.
     * @return the detection performed
     */
    public Check getDetection(){
        return this.detection;
    }

    @Override
    public GenericResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addViolationGlobal(InfractionType type){
        switch(type){
            case SPAM: spamCount++; break;
            case FLOOD: floodCount++; break;
            case REGULAR: regularCount++; break;
            case BCOMMAND: commandCount++; break;
            case UNICODE: unicodeViolations++; break;
            case NONE: break;
        }
    }
}
