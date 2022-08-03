package me.adrianed.chatregulator.api.configuration.blacklist;

import java.util.List;
import java.util.regex.Pattern;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import net.kyori.adventure.text.TextReplacementConfig;

@ConfigSerializable
public class ComponentBlacklist implements Blacklist<TextReplacementConfig> {
    private List<TextReplacementConfig> configs = List.of(
        TextReplacementConfig.builder().match(Pattern.compile("a")).replacement("b").build()
    );

    @Override
    public List<TextReplacementConfig> blacklist() {
        return this.configs;
    }
    
}
