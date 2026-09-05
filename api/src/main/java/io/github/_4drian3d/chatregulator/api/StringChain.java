package io.github._4drian3d.chatregulator.api;

import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Commands and Messages Execution Chain from a player
 */
public interface StringChain extends Iterable<@NotNull String> {
    @NotNull String first();

    @NotNull String last();

    @NotNull Instant lastExecuted();

    @NonNegative int size();
}
