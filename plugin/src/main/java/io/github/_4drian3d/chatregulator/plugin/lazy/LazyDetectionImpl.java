package io.github._4drian3d.chatregulator.plugin.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.LazyDetection;
import io.github._4drian3d.chatregulator.api.checks.ICheck;
import io.github._4drian3d.chatregulator.api.result.CheckResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LazyDetectionImpl implements LazyDetection {
    private final CheckProvider<? extends ICheck>[] checks;

    LazyDetectionImpl(final CheckProvider<? extends ICheck>[] checks) {
        this.checks = checks;
    }

    @Override
    public CompletableFuture<CheckResult> detect(final InfractionPlayer player, final String string) {
        return CompletableFuture.supplyAsync(() -> {
            AtomicReference<String> modifiedString = new AtomicReference<>(string);
            for (final CheckProvider<? extends ICheck> provider : checks) {
                final ICheck providedCheck = provider.provide(player);
                if (providedCheck == null) {
                    continue;
                }
                final CheckResult result = providedCheck.check(player, string);
                if (result.isAllowed()) {
                    continue;
                }

                if (result.isDenied()) {
                    return result;
                }

                if (result.shouldModify() && result instanceof CheckResult.ReplaceCheckResult replaceCheckResult) {
                    modifiedString.set(replaceCheckResult.replaced());
                }
            }
            if (modifiedString.get().equals(string)) {
                return CheckResult.modified(modifiedString.get());
            }
            return CheckResult.allowed();
        });
    }
}
