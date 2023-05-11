package io.github._4drian3d.chatregulator.plugin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public final class Components {

    /**
     * Spaces component for "/chatregulator clear" command
     */
    public static final Component SPACES_COMPONENT;

    static {
        TextComponent.Builder builder = Component.text();
        for(int i = 0; i < 100; i++){
            builder.append(Component.newline());
        }
        SPACES_COMPONENT = builder.build();
    }
    private Components(){}
}