package io.github._4drian3d.chatregulator.utils;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.chatregulator.objects.TestPlayer;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;

public final class TestsUtils {
    public static InfractionPlayerImpl dummyPlayer() {
        return new InfractionPlayerImpl(new TestPlayer("", false), null);
    }
    public static InfractionPlayerImpl createNormalPlayer(String name){
        Player player = new TestPlayer(name, false);

        return new InfractionPlayerImpl(player, null);
    }
}
