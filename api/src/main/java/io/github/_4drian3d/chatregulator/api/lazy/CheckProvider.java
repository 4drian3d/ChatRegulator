package io.github._4drian3d.chatregulator.api.lazy;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.checks.Check;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Functional interface for providing check instances.
 * <br>
 * This interface is used as a lazy detection to create check instances
 * on-demand for a specific player.
 *
 * @param <C> the type of check provided
 */
public interface CheckProvider<C extends Check> {
    /**
     * Provides a check instance for the specified player.
     * <br>
     * Implementations can return null if no check should be executed for this player.
     *
     * @param player the player for whom to provide a check
     * @return a check instance for this player, or null if no check should be applied
     */
    @Nullable C provide(@NonNull InfractionPlayer player);
}
