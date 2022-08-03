package me.adrianed.chatregulator.api.configuration.blacklist;

import java.util.List;
import java.util.regex.Pattern;

public class PatternBlacklist implements Blacklist<Pattern> {
    private List<Pattern> patterns = List.of(
        Pattern.compile("a")
    );

    @Override
    public List<Pattern> blacklist() {
        return this.patterns;
    }
    
}
