package net.dreamerzero.chatregulator.listener;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import net.dreamerzero.chatregulator.Regulator;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class ChatListener {
    private final ProxyServer server;
    private final Logger logger;

    public ChatListener(final ProxyServer server, final Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onChat(final PlayerChatEvent event) {
        String message = event.getMessage();
        var player = event.getPlayer();

        List<Template> TEMPLATES = List.of(
            Template.of("player", player.getUsername()),
            Template.of("message", message),
            Template.of("server", player.getCurrentServer().get().getServerInfo().getName()));

        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        String floodRegexPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)".replace("<l>", Regulator.getConfig().getString("flood.limit"));

        Matcher floodMatch = Pattern.compile(floodRegexPattern).matcher(message);
        if(floodMatch.find()) {
            event.setResult(ChatResult.denied());
                player.sendMessage(
                    MiniMessage.get().parse(
                        Regulator.getConfig().getString("messages.flood-message"), TEMPLATES));
                server.getAllPlayers().stream().filter(
                    op -> op.hasPermission("regulator.notifications")).forEach(op -> {
                        op.sendMessage(
                            MiniMessage.get().parse(
                                Regulator.getConfig().getString("messages.flood-detected"), TEMPLATES));
                    });
        }

        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(message);

            if(match.find()) {
                event.setResult(ChatResult.denied());
                player.sendMessage(
                    MiniMessage.get().parse(
                        Regulator.getConfig().getString("messages.blocked-message"), TEMPLATES));
                server.getAllPlayers().stream().filter(
                    op -> op.hasPermission("regulator.notifications")).forEach(op -> {
                        op.sendMessage(
                            MiniMessage.get().parse(
                                Regulator.getConfig().getString("messages.infraction-detected"), TEMPLATES));
                    });
                if (Regulator.getConfig().getBoolean("debug")){
                    logger.info("User Detected: " + player.getUsername());
                    logger.info("Message: " + message);
                    logger.info("Pattern: " + floodMatch.pattern());
                    logger.info("Results: " + floodMatch.results().toList().toString());
                }
                break;
            }
        }
    }
}
