package me.dreamerzero.chatregulator.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;

public final class Components {
    public static final MiniMessage MESSAGE_MINIMESSAGE = MiniMessage.builder()
        .transformations(tr -> tr.clear()
            .add(TransformationType.COLOR)
            .add(TransformationType.RAINBOW)
            .add(TransformationType.DECORATION)
            .add(TransformationType.FONT)
            .add(TransformationType.GRADIENT)
            .add(TransformationType.HOVER_EVENT)
            .add(TransformationType.INSERTION)
            .add(TransformationType.KEYBIND)
            .add(TransformationType.RAINBOW)
            .add(TransformationType.TRANSLATABLE)
            .build()
        ).build();
    public static final MiniMessage SPECIAL_MINIMESSAGE = MiniMessage.builder()
        .transformations(tr -> tr.clear()
            .add(TransformationType.COLOR)
            .add(TransformationType.DECORATION)
            .add(TransformationType.FONT)
            .add(TransformationType.GRADIENT)
            .add(TransformationType.KEYBIND)
            .add(TransformationType.RAINBOW)
            .add(TransformationType.TRANSLATABLE)
            .build()
        ).build();

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
