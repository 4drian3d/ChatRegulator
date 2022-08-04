package me.adrianed.chatregulator.api.configuration;

import java.util.List;
import java.util.regex.Pattern;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Blacklist {
    private List<Pattern> patterns = List.of(Pattern.compile(""));

    public List<Pattern> patterns() {
        return this.patterns;
    }
}
