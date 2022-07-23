package me.dreamerzero.chatregulator.listener.plugin;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;

/**Proxy reload listener */
@Internal
public final class ReloadListener {
    private final ChatRegulator plugin;
    public ReloadListener(ChatRegulator plugin){
        this.plugin = plugin;
    }

    /**
     * On proxy reload
     * @param event the event
     */
    @Subscribe
    public void onReload(ProxyReloadEvent event){
        plugin.reloadConfig();
    }
}
