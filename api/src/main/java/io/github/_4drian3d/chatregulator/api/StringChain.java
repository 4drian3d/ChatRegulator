package io.github._4drian3d.chatregulator.api;

import java.time.Instant;
import java.util.Optional;

public interface StringChain extends Iterable<String> {
    Optional<String> index(int index);

    String first();

    String last();

    Instant lastExecuted();
}
