package net.dreamerzero.chatregulator.listener.plugin;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;

import org.slf4j.Logger;

import net.frankheijden.serverutils.velocity.events.VelocityPluginEnableEvent;

public class PluginListener {
    private Logger logger;
    public PluginListener(Logger logger){
        this.logger = logger;
    }
    @Subscribe(order = PostOrder.LATE)
    public void onPluginEnable(VelocityPluginEnableEvent enableEvent){
        if(enableEvent.getPlugin().getDescription().getName().get().equalsIgnoreCase("ChatRegulator")){
            logger.warn("ChatRegulator has been reloaded via ServerUtils");
            logger.warn("Although this is safe, the warnings of all InfractionPlayers will be deleted.");
        }
    }
}
