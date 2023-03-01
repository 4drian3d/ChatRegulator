package io.github._4drian3d.chatregulator.plugin;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.api.PlayerManager;

import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {
    @Override
    public InfractionPlayerImpl getPlayer(UUID uuid) {
        return null;
    }

    public InfractionPlayerImpl getPlayer(Player player) {
        return null;
    }

    public InfractionPlayerImpl getPlayer(String name) {
        return null;
    }
}
