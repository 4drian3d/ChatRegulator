package me.dreamerzero.chatregulator.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public final class Components {
    public static final MiniMessage MESSAGE_MINIMESSAGE = MiniMessage.builder()
        .tags(TagResolver.resolver(
            StandardTags.color(),
            StandardTags.decorations(),
            StandardTags.rainbow(),
            StandardTags.font(),
            StandardTags.gradient(),
            StandardTags.hoverEvent(),
            StandardTags.insertion(),
            StandardTags.keybind(),
            StandardTags.translatable(),
            StandardTags.clickEvent()
        )).build();
    public static final MiniMessage SPECIAL_MINIMESSAGE = MiniMessage.builder()
        .tags(TagResolver.resolver(
            StandardTags.color(),
            StandardTags.decorations(),
            StandardTags.font(),
            StandardTags.gradient(),
            StandardTags.keybind(),
            StandardTags.translatable()
        )).build();

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
