package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import org.jetbrains.annotations.NotNull;

/**
 * Base class of the checks used in the plugin
 */
public sealed interface Check permits CapsCheck, CommandCheck, CooldownCheck, FloodCheck, RegexCheck, SpamCheck, SyntaxCheck, UnicodeCheck {
    /**
     * Check if the delivered string contains any infraction
     * and returns a CompletableFuture with the corresponding CheckResult
     * To see what check has returned, perform a check by instanceof
     * @param string the string to check
     * @see CheckResult
     * @since 3.0.0
     * @return a CompletableFuture with the result of the check
     */
    @NotNull CheckResult check(final @NotNull InfractionPlayer player, final @NotNull String string);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    @NotNull InfractionType type();
}
