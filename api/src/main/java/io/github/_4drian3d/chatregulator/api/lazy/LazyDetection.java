package io.github._4drian3d.chatregulator.api.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.Check;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public final class LazyDetection {
    private final CheckProvider<? extends Check>[] checks;

    LazyDetection(final CheckProvider<? extends Check>[] checks) {
        this.checks = checks;
    }

    @SafeVarargs
    public static LazyDetection checks(final CheckProvider<? extends Check>... checks) {
        return new LazyDetection(checks);
    }

    public @NotNull CompletableFuture<CheckResult> detect(final @NotNull InfractionPlayer player, final @NotNull String string) {
        return CompletableFuture.supplyAsync(() -> {
            final AtomicReference<InfractionDetection> modifiedString = new AtomicReference<>();
            for (final CheckProvider<? extends Check> provider : checks) {
                final Check providedCheck = provider.provide(player);
                if (providedCheck == null) {
                    continue;
                }
                final CheckResult result = providedCheck.check(player, modifiedOrDefault(modifiedString, string));
                if (result.isAllowed()) {
                    continue;
                }

                if (result.isDenied()) {
                    return result;
                }

                if (result instanceof final CheckResult.ReplaceCheckResult replaceCheckResult) {
                    modifiedString.set(new InfractionDetection(replaceCheckResult.infractionType(), replaceCheckResult.replaced()));
                }
            }
            final InfractionDetection finalResult = modifiedString.get();
            if (finalResult == null) {
                return CheckResult.allowed();
            }
            if (!Objects.equals(finalResult.modified, string)) {
                return CheckResult.modified(finalResult.infractionType, finalResult.modified);
            }
            return CheckResult.allowed();
        });
    }

    private @NotNull String modifiedOrDefault(final @NotNull AtomicReference<InfractionDetection> reference, final @NotNull String defaultValue) {
        final InfractionDetection actualDetection = reference.get();
        if (actualDetection == null) {
            return defaultValue;
        }
        return actualDetection.modified;
    }

    private record InfractionDetection(@NotNull InfractionType infractionType, @NotNull String modified) {}
}
