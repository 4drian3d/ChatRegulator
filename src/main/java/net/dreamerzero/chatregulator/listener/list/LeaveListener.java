package net.dreamerzero.chatregulator.listener.list;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import net.dreamerzero.chatregulator.InfractionPlayer;

public class LeaveListener {
    @Subscribe(async = true)
    public void onLeave(DisconnectEvent event){
        Player player = event.getPlayer();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);
        infractionPlayer.isOnline(false);
    }
}
