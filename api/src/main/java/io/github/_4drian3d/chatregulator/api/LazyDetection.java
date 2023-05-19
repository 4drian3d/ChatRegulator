package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface LazyDetection {
    @NotNull CompletableFuture<CheckResult> detect(final @NotNull InfractionPlayer player, final @NotNull String string);
}
