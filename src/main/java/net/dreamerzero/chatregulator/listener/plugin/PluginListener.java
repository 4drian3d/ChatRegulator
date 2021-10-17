package net.dreamerzero.chatregulator.listener.plugin;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.frankheijden.serverutils.common.events.PluginEvent.Stage;
import net.frankheijden.serverutils.velocity.events.VelocityPluginEnableEvent;

/**
 * ServerUtils Listener in case ChatRegulator is reloaded
 */
public class PluginListener {
    private final Logger logger;
    private final PluginDescription plinfo;
    /**
     * Plugin Listener constructor
     * @param logger
     */
    public PluginListener(Logger logger, ProxyServer server){
        this.logger = logger;
        this.plinfo = server.getPluginManager().getPlugin("chatregulator").get().getDescription();
    }
    /**
     * Enable Listener
     * In case ChatRegulator is reloaded,
     * a warning will be sent that all
     * {@link net.dreamerzero.chatregulator.InfractionPlayer} will be restarted.
     * @param enableEvent the enable event
     */
    @Subscribe(order = PostOrder.LATE)
    public void onPluginEnable(VelocityPluginEnableEvent enableEvent){
        if(enableEvent.getStage() == Stage.POST && enableEvent.getPlugin().getDescription().equals(plinfo)){
            logger.warn("ChatRegulator has been reloaded via ServerUtils");
            logger.warn("Although this is safe, the warnings of all InfractionPlayers will be deleted.");
        }
    }
}
