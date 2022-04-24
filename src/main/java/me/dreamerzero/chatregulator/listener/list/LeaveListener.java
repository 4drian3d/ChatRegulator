package me.dreamerzero.chatregulator.listener.list;

import java.util.UUID;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import org.jetbrains.annotations.ApiStatus.Internal;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;

@Internal
public final class LeaveListener {
    private final ChatRegulator plugin;
    public LeaveListener(ChatRegulator plugin) {
        this.plugin = plugin;
    }
    /**
     * In case the player leaves the server,
     * his online status is marked as false.
     * @param event the event
     */
    @Subscribe(order = PostOrder.LAST)
    public EventTask onLeave(final DisconnectEvent event){
        return EventTask.async(() -> {
            final InfractionPlayer player = InfractionPlayer.get(event.getPlayer());
            player.isOnline(false);

            final UUID uuid = event.getPlayer().getUniqueId();

            plugin.getProxy().getScheduler().buildTask(plugin, () -> {
                if(plugin.getProxy().getPlayer(uuid).isEmpty()) {
                    plugin.getLogger().debug("The player {} was eliminated", plugin.getChatPlayers().remove(uuid).username());
                }
            }).delay(
                Configuration.getConfig().getGeneralConfig().deleteUsersTime(),
                Configuration.getConfig().getGeneralConfig().unit()
            ).schedule();
        });
    }
}
