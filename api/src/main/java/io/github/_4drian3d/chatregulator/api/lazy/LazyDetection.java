package io.github._4drian3d.chatregulator.api.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.Check;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lazy detection utility class for running multiple checks asynchronously.
 * <br>
 * This class allows executing multiple checks in sequence, where each check can
 * modify the input string for subsequent checks. Checks are provided lazily via
 * {@link CheckProvider}, allowing for player-specific or conditional check execution.
 * <br>
 * The detection process runs asynchronously and returns results via CompletableFuture.
 * <ul>
 *  <li>If any check denies the message, detection stops and returns the denied result</li>
 *  <li>If a check requests modification, subsequent checks operate on the modified string</li>
 *  <li>If all checks pass or modify without denial, a modified or allowed result is returned</li>
 * </ul>
 *
 * @see CheckResult
 * @see CheckProvider
 * @since 4.0.1
 */
public final class LazyDetection {
    private final CheckProvider<? extends Check>[] checks;

    LazyDetection(final CheckProvider<? extends Check>[] checks) {
        this.checks = checks;
    }

    /**
     * Creates a LazyDetection instance with the specified check providers.
     * 
     * @param checks the check providers to execute in sequence
     * @return a new LazyDetection instance
     * @since 4.0.1
     */
    @SafeVarargs
    public static LazyDetection checks(final CheckProvider<? extends Check>... checks) {
        return new LazyDetection(checks);
    }

    /**
     * Asynchronously detects infractions in the provided string using configured checks.
     * 
     * <br>Checks are executed sequentially, where each check can either:
     * <ul>
     *  <li>Allow the message to pass ({@link CheckResult#isAllowed} = true)</li>
     *  <li>Deny the message ({@link CheckResult#isDenied} = true)</li>
     *  <li>Modify the message for subsequent checks ({@link CheckResult#shouldModify} = true)</li>
     * </ul>
     *
     * If a modification occurs, subsequent checks operate on the modified string.
     * The method tracks whether the final string differs from the original.
     *
     * @param player the player performing the action
     * @param string the message or command to check
     * @return a CompletableFuture containing the check result (allowed, denied, or modified)
     * @since 4.0.1
     */
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

                if (result instanceof CheckResult.ReplaceCheckResult(InfractionType infractionType, String replaced)) {
                    modifiedString.set(new InfractionDetection(infractionType, replaced));
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

    /**
     * Returns the modified string from the reference if available, otherwise returns the default value.
     * <br>
     * This helper method is used to chain check results, where subsequent checks
     * operate on the modified string if a previous check modified it.
     *
     * @param reference the atomic reference containing the current detection state
     * @param defaultValue the default string to use if no modification has occurred
     * @return the modified string or the default value
     */
    private @NotNull String modifiedOrDefault(final @NotNull AtomicReference<InfractionDetection> reference, final @NotNull String defaultValue) {
        final InfractionDetection actualDetection = reference.get();
        if (actualDetection == null) {
            return defaultValue;
        }
        return actualDetection.modified;
    }

    private record InfractionDetection(@NotNull InfractionType infractionType, @NotNull String modified) {}
}
