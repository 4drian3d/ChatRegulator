package io.github._4drian3d.chatregulator.api;

import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Commands and Messages Execution Chain from a player
 */
public interface StringChain extends Iterable<@NotNull String> {
    /**
     * Returns the first element in the chain.
     *
     * @return the first command or message in the chain
     */
    @NotNull String first();

    /**
     * Returns the last element in the chain.
     *
     * @return the most recent command or message in the chain
     */
    @NotNull String last();

    /**
     * Returns the instant when the last command or message was executed.
     *
     * @return the timestamp of the last execution
     */
    @NotNull Instant lastExecuted();

    /**
     * Returns the number of elements in this chain.
     *
     * @return the size of the chain, never negative
     */
    @NonNegative int size();
}
