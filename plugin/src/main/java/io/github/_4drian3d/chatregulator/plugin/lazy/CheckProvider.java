package io.github._4drian3d.chatregulator.plugin.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.ICheck;

public interface CheckProvider<C extends ICheck> {
    C provide(InfractionPlayer player);
}
