package io.github._4drian3d.chatregulator.plugin.impl;

import com.google.inject.Inject;
import io.github._4drian3d.chatregulator.api.StringChain;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public final class StringChainImpl implements StringChain {
    private final LinkedList<String> queue = new LinkedList<>();
    private final AtomicReference<Instant> lastExecuted = new AtomicReference<>(Instant.now());
    @Inject
    private ConfigurationContainer<Configuration> configurationContainer;
    @Override
    public String index(int index) {
        return queue.get(index);
    }

    @Override
    public String first() {
        return queue.getFirst();
    }

    @Override
    public String last() {
        return queue.getLast();
    }

    @Override
    public Instant lastExecuted() {
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
        if (configurationContainer != null && configurationContainer.get().getSpamConfig().getSimilarStringCount() <= queue.size()) {
            queue.removeFirst();
        }
        queue.add(string);
        lastExecuted.set(Instant.now());
    }
}
