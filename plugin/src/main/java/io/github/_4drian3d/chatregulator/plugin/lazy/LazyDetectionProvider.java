package io.github._4drian3d.chatregulator.plugin.lazy;

import io.github._4drian3d.chatregulator.api.LazyDetection;
import io.github._4drian3d.chatregulator.api.checks.ICheck;
import io.github._4drian3d.chatregulator.plugin.CheckProvider;

public class LazyDetectionProvider {

    @SafeVarargs
    public static LazyDetection checks(CheckProvider<? extends ICheck>... checks) {
        return new LazyDetectionImpl(checks);
    }
}
