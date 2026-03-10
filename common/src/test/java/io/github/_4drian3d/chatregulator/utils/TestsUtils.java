package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.object.TestInfractionPlayer;

public final class TestsUtils {
    public static InfractionPlayer dummyPlayer() {
        return TestInfractionPlayer.create(null);
    }

    public static InfractionPlayer playerFrom(ConfigurationContainer<Checks> configurationContainer) {
        return TestInfractionPlayer.create(configurationContainer);
    }
}
