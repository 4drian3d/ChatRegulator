package net.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;

import de.leonhard.storage.Yaml;
import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.events.ChatViolationEvent;
import net.dreamerzero.chatregulator.modules.Replacer;
import net.dreamerzero.chatregulator.modules.checks.Check;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;
import net.kyori.adventure.audience.Audience;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatListener {

    private final ProxyServer server;
    private final ConfigManager cManager;
    private final CommandUtils cUtils;
    private final DebugUtils dUtils;
    private final FloodCheck fUtils;
    private final InfractionCheck iUtils;
    private final Yaml config;
    private final Replacer rUtils;

    /**
     * ChatListener Constructor
     * @param server the proxy server
     * @param logger the logger
     * @param config the plugin config
     * @param blacklist the blacklist config
     */
    public ChatListener(final ProxyServer server, Logger logger, Yaml config, Yaml blacklist, Yaml messages) {
        this.server = server;
        this.cManager = new ConfigManager(messages);
        this.cUtils = new CommandUtils(server, config);
        this.dUtils = new DebugUtils(logger, config);
        this.fUtils = new FloodCheck(config);
        this.iUtils = new InfractionCheck(blacklist);
        this.rUtils = new Replacer(config);
        this.config = config;
    }

    /**
     * Chat Listener for detections
     * @param event the chat event
     */
    @Subscribe(async = true)
    public void onChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        fUtils.check(message);
        if(config.getBoolean("flood.enabled") &&
            !player.hasPermission("chatregulator.bypass.flood")
            && fUtils.isInfraction()) {

            if(!callChatViolationEvent(infractionPlayer, message, InfractionType.FLOOD, fUtils)) {
                event.setResult(ChatResult.denied());
                return;
            }
        }

        iUtils.check(message);
        if(config.getBoolean("infractions.enabled") &&
            !player.hasPermission("chatregulator.bypass.infractions") &&
            iUtils.isInfraction()) {

            if(!callChatViolationEvent(infractionPlayer, message, InfractionType.REGULAR, iUtils)) {
                event.setResult(ChatResult.denied());
                return;
            }
        }

        SpamCheck sUtils = new SpamCheck(infractionPlayer, SourceType.CHAT);
        sUtils.check(message);
        if(config.getBoolean("spam.enabled") &&
            !player.hasPermission("chatregulator.bypass.spam") &&
            sUtils.isInfraction()) {

            if(!callChatViolationEvent(infractionPlayer, message, InfractionType.SPAM, sUtils)) {
                event.setResult(ChatResult.denied());
                return;
            }
        }

        if(config.getBoolean("format.enabled")){
            String formatted = rUtils.applyFormat(message);
            infractionPlayer.lastMessage(formatted);
            event.setResult(ChatResult.message(formatted));
            return;
        }

        infractionPlayer.lastMessage(message);
    }

    /**
     * Call chat violation event
     * and approves player message
     * @param player Player who send the message
     * @param message The message
     * @param type InfractionType to check
     * @author Espryth
     * @return message of {@link PlayerChatEvent} is approved
     */
    private boolean callChatViolationEvent(InfractionPlayer player, String message, InfractionType type, Check detection) {
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new ChatViolationEvent(player, type, detection, message)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(message);
            } else {
                approved.set(false);
                dUtils.debug(player, message, type);
                violationEvent.addViolationGlobal(type);
                cManager.sendWarningMessage(player, type);
                //TODO: Change this in java 16 update
                /*
                cManager.sendAlertMessage(Audience.audience(server.getAllPlayers().stream().filter(
                  op -> op.hasPermission("chatregulator.notifications")).toList()), player, type);
                */
                ArrayList<Audience> playersOp = new ArrayList<>();
                for(Player playerOp : server.getAllPlayers()){
                    if(playerOp.hasPermission("chatregulator.notifications")){
                        playersOp.add(playerOp);
                    }
                }
                cManager.sendAlertMessage(Audience.audience(playersOp), player, type);
                player.addViolation(type);
                cUtils.executeCommand(type, player);
            }
        });
        return approved.get();
    }

}
