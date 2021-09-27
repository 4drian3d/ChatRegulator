package net.dreamerzero.chatregulator.listener;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
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
        Player player = event.getPlayer();
        String patternDetected = "";
        String detection = "";
        boolean detected = false;

        List<Template> TEMPLATES = List.of(
            Template.of("player", player.getUsername()),
            Template.of("message", message),
            Template.of("server", player.getCurrentServer().get().getServerInfo().getName()));

        List<String> blockedWords = Regulator.getBlackList().getStringList("blocked-words");
        String floodPattern = "(\\w)\\1{<l>,}|(\\w{28,})|([^\\wñ]{20,})|(^.{220,}$)".replace("<l>", Regulator.getConfig().getString("flood.limit"));

        Matcher floodMatch = Pattern.compile(floodPattern).matcher(message);
        if(floodMatch.find()) {
            event.setResult(ChatResult.denied());
            player.sendMessage(
                MiniMessage.miniMessage().parse(
                    Regulator.getConfig().getString("messages.flood-message"), TEMPLATES));
            server.getAllPlayers().stream().filter(
                op -> op.hasPermission("regulator.notifications")).forEach(op -> {
                    op.sendMessage(
                        MiniMessage.miniMessage().parse(
                            Regulator.getConfig().getString("messages.flood-detected"), TEMPLATES));
                });
            patternDetected = floodPattern;
            detection = "Flood";
            detected = true;
        }

        for (String blockedWord : blockedWords){
            Matcher match = Pattern.compile(blockedWord).matcher(message);

            if(match.find()) {
                event.setResult(ChatResult.denied());
                player.sendMessage(
                    MiniMessage.miniMessage().parse(
                        Regulator.getConfig().getString("messages.blocked-message"), TEMPLATES));
                server.getAllPlayers().stream().filter(
                    op -> op.hasPermission("regulator.notifications")).forEach(op -> {
                        op.sendMessage(
                            MiniMessage.miniMessage().parse(
                                Regulator.getConfig().getString("messages.infraction-detected"), TEMPLATES));
                    });
                patternDetected = blockedWord;
                detection = "Regular infraction";
                detected = true;
                break;
            }
        }

        if (Regulator.getConfig().getBoolean("debug") && detected){
            logger.info("User Detected: {}", player.getUsername());
            logger.info("Detection: {}", detection);
            logger.info("Message: {}", message);
            logger.info("Pattern: {}", patternDetected);
        }
    }
}
