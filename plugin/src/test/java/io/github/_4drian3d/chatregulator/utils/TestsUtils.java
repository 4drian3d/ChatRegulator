package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.objects.TestPlayer;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;

public final class TestsUtils {
    public static InfractionPlayerImpl dummyPlayer() {
        final var player = new TestPlayer("", false);

        return new InfractionPlayerImpl(
                player.getUniqueId(),
                uuid -> player,
                null,
                null,
                null,
                null
        );
    }

    public static InfractionPlayerImpl playerFrom(ConfigurationContainer<Checks> configurationContainer) {
        final var player = new TestPlayer("", false);

        return new InfractionPlayerImpl(
                player.getUniqueId(),
                uuid -> player,
                null,
                configurationContainer,
                null,
                null
        );
    }
}
