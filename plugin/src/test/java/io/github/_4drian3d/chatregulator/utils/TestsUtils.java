package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.objects.TestAudience;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;

import java.util.UUID;

public final class TestsUtils {
    public static InfractionPlayerImpl dummyPlayer() {
        final var player = new TestAudience("");

        return new InfractionPlayerImpl(
            UUID.randomUUID(),
                uuid -> player,
                null,
                null,
                null,
                null
        );
    }

    public static InfractionPlayerImpl playerFrom(ConfigurationContainer<Checks> configurationContainer) {
        final var player = new TestAudience("");

        return new InfractionPlayerImpl(
            UUID.randomUUID(),
                uuid -> player,
                null,
                configurationContainer,
                null,
                null
        );
    }
}
