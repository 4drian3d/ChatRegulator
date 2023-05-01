package io.github._4drian3d.chatregulator.api;

import io.github._4drian3d.chatregulator.api.result.CheckResult;

import java.util.concurrent.CompletableFuture;

public interface LazyDetection {
    CompletableFuture<CheckResult> detect(InfractionPlayer player, String string);
}
