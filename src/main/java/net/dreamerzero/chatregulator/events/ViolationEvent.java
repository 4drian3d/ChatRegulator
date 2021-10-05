package net.dreamerzero.chatregulator.events;

import java.util.Objects;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;

import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;

public class ViolationEvent implements ResultedEvent<GenericResult> {
    protected final InfractionPlayer infractionPlayer;
    protected final InfractionType type;
    private GenericResult result = GenericResult.allowed();

    /**
     * ViolationEvent Contructor
     * @param infractionPlayer the player who committed the infraction
     * @param type the infraction type
     */
    public ViolationEvent(InfractionPlayer infractionPlayer, InfractionType type){
        this.infractionPlayer = infractionPlayer;
        this.type = type;
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

    @Override
    public GenericResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
