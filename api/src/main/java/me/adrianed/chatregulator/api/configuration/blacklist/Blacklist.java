package me.adrianed.chatregulator.api.configuration.blacklist;

import java.util.List;

public interface Blacklist<A> {
    String HEADER = """
            ChatRegulator | by 4drian3d
            a
            """;
    List<A> blacklist();
}
