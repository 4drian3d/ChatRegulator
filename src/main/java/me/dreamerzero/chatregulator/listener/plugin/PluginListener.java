package me.dreamerzero.chatregulator.listener.plugin;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

import net.frankheijden.serverutils.common.events.PluginEvent.Stage;
import net.frankheijden.serverutils.velocity.events.VelocityPluginEnableEvent;

/**
 * ServerUtils Listener in case ChatRegulator is reloaded
 */
@Internal
public class PluginListener {
    private final Logger logger;
    private static final String PLNAME = "chatregulator";
    /**
     * Plugin Listener constructor
     * @param logger the logger
     */
    public PluginListener(Logger logger){
        this.logger = logger;
    }
    /**
     * Enable Listener
     * In case ChatRegulator is reloaded,
     * a warning will be sent that all
     * {@link me.dreamerzero.chatregulator.InfractionPlayer} will be restarted.
     * @param enableEvent the enable event
     */
    @Subscribe(order = PostOrder.LATE)
    public void onPluginEnable(VelocityPluginEnableEvent enableEvent){
        if(enableEvent.getStage() == Stage.POST && enableEvent.getPlugin().getDescription().getId().equals(PLNAME)){
            logger.warn("ChatRegulator has been reloaded via ServerUtils");
            logger.warn("Although this is safe, the warnings of all InfractionPlayers will be deleted.");
        }
    }
}
