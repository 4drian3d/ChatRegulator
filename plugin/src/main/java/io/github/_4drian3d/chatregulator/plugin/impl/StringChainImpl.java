package io.github._4drian3d.chatregulator.plugin.impl;

import com.google.inject.Inject;
import io.github._4drian3d.chatregulator.api.StringChain;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class StringChainImpl implements StringChain {
    private final ArrayDeque<String> queue = new ArrayDeque<>();
    private final AtomicReference<Instant> lastExecuted = new AtomicReference<>(Instant.now());
    private final Lock lock = new ReentrantLock();
    @Inject
    private ConfigurationContainer<Checks> checksContainer;

    @Override
    public @NotNull String first() {
        return queue.getFirst();
    }

    @Override
    public @NotNull String last() {
        return queue.getLast();
    }

    @Override
    public @NotNull Instant lastExecuted() {
        return lastExecuted.get();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return queue.iterator();
    }

    public void executed(final String string) {
        lock.lock();
        try {
            if (checksContainer != null) {
                final Checks.Spam spamConfig = checksContainer.get().getSpamConfig();
                final int similarCount = spamConfig.getSimilarStringCount();
                final int size = queue.size();
                // If at the moment, the configured limit is negative or non-existent,
                // it simply deletes the stored information and avoids further checks
                if (!spamConfig.enabled() || similarCount < 1) {
                    // If there is information in the queue, it is removed to eliminate a possible resource leak
                    if (size != 0) {
                        queue.clear();
                    }
                    return;
                }
                // If there are no elements added to the queue yet, simply add the first one
                if (size == 0) {
                    addExecution(string);
                    return;
                }
                // If the configured limit has changed and there are more elements than configured,
                // the oldest one is deleted until there are the same number of required elements or less
                if (similarCount < size) {
                    while (similarCount < queue.size() + 1) {
                        queue.removeFirst();
                    }
                } else if (similarCount == size) {
                    // If the configured limit is reached, the oldest element is deleted
                    queue.removeFirst();
                }
            }
            addExecution(string);
        } finally {
            lock.unlock();
        }
    }

    private void addExecution(String string) {
        queue.add(string);
        lastExecuted.set(Instant.now());
    }
}
