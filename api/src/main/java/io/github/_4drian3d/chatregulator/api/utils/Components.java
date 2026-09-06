package io.github._4drian3d.chatregulator.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * Utility class for creating and managing common text components.
 */
public final class Components {

    /**
     * A component containing multiple newlines for clearing the chat.
     * <br>
     * Used by the {@code /chatregulator clear} command
     * to visually clear the player's chat.
     */
    public static final Component SPACES_COMPONENT;

    static {
        final TextComponent.Builder builder = Component.text();
        for (int i = 0; i < 100; i++) {
            builder.appendNewline();
        }
        SPACES_COMPONENT = builder.build().compact();
    }
    private Components() {}
}
