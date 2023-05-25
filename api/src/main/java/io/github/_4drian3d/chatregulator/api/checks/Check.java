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
     * Check if the provided string contains any infraction
     * and returns the corresponding CheckResult
     *
     * @param string the string to check
     * @param player the player
     * @see CheckResult
     * @since 3.0.0
     * @return a result from the check
     */
    @NotNull CheckResult check(final @NotNull InfractionPlayer player, final @NotNull String string);

    /**
     * Get the {@link InfractionType} of this check
     * @return the infraction type
     */
    @NotNull InfractionType type();
}
