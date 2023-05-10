package io.github._4drian3d.chatregulator.api;

import java.time.Instant;

public interface StringChain extends Iterable<String> {
    String index(int index);

    String first();

    String last();

    Instant lastExecuted();

    int size();
}
