package io.github._4drian3d.chatregulator.plugin.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.Check;

public interface CheckProvider<C extends Check> {
    C provide(InfractionPlayer player);
}
