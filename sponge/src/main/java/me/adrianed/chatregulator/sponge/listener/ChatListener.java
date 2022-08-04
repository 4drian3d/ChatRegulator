package me.adrianed.chatregulator.sponge.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.PlayerChatEvent;

import me.adrianed.chatregulator.sponge.SpongePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ChatListener {
    private final SpongePlugin plugin;
    public ChatListener(SpongePlugin plugin) {
        this.plugin = plugin;
    }
    @Listener
    public void onChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        Matcher matcher;
        for (Pattern pattern : plugin.blacklist().patterns()) {
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                event.setMessage(event.message().replaceText(builder ->
                    builder.match(pattern)
                        .replacement((a, b) -> Component.text("***"))));
                //event.setCancelled(true);
            }
        }
    }
}
