package io.github._4drian3d.chatregulator.utils;

import io.github._4drian3d.chatregulator.objects.TestPlayer;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;

public final class TestsUtils {
    public static InfractionPlayerImpl dummyPlayer() {
        return new InfractionPlayerImpl(new TestPlayer("", false), null);
    }
}
