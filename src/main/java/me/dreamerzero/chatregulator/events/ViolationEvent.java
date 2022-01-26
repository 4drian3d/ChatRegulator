package me.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Basis for infringement events
 */
public class ViolationEvent implements ResultedEvent<GenericResult> {
    /**
     * InfractionPlayer involved in detection
     */
    private final InfractionPlayer infractionPlayer;
    /**
     * Type of detection
     */
    private final InfractionType type;
    private final AbstractCheck detection;
    private GenericResult result = GenericResult.allowed();

    /**
     * ViolationEvent Contructor
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     * @param detection the detection
     */
    @Internal
    public ViolationEvent(@NotNull InfractionPlayer infractionPlayer, @NotNull InfractionType type, @NotNull AbstractCheck detection){
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
    public @NotNull InfractionPlayer getInfractionPlayer(){
        return this.infractionPlayer;
    }

    /**
     * Get the type of infraction committed
     * @return the infraction committed
     * @since 1.1.0
     */
    public @NotNull InfractionType getType(){
        return this.type;
    }

    /**
     * Obtain the detection performed
     * With this object, you can get the pattern,
     * the detected string and more.
     * @return the detection performed
     */
    public @NotNull AbstractCheck getDetection(){
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
}
