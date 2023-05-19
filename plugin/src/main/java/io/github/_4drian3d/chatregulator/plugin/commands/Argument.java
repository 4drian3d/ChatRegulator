package io.github._4drian3d.chatregulator.plugin.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.CommandSource;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Collection;

public interface Argument {
    CommandNode<CommandSource> node();

    default void sendLines(Audience sender, Collection<String> lines, TagResolver resolver, IFormatter formatter) {
        lines.forEach(ln -> sender.sendMessage(formatter.parse(ln, sender, resolver)));
    }

    default LiteralArgumentBuilder<CommandSource> literal(String command) {
        return LiteralArgumentBuilder.literal(command);
    }

    default <E> RequiredArgumentBuilder<CommandSource, E> argument(String command, ArgumentType<E> argumentType) {
        return RequiredArgumentBuilder.argument(command, argumentType);
    }
}
