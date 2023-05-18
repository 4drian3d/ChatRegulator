package io.github._4drian3d.chatregulator.api;

import org.jetbrains.annotations.NotNull;

public interface ChatRegulatorAPI {
    @NotNull PlayerManager getPlayerManager();

    @NotNull Statistics getStatistics();
}