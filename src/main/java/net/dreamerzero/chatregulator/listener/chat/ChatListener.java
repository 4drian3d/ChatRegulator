package net.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;


import net.dreamerzero.chatregulator.InfractionPlayer;
import net.dreamerzero.chatregulator.config.ConfigManager;
import net.dreamerzero.chatregulator.config.Configuration;
import net.dreamerzero.chatregulator.config.MainConfig;
import net.dreamerzero.chatregulator.events.ChatViolationEvent;
import net.dreamerzero.chatregulator.modules.Replacer;
import net.dreamerzero.chatregulator.modules.Statistics;
import net.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import net.dreamerzero.chatregulator.modules.checks.FloodCheck;
import net.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import net.dreamerzero.chatregulator.modules.checks.SpamCheck;
import net.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import net.dreamerzero.chatregulator.utils.CommandUtils;
import net.dreamerzero.chatregulator.utils.DebugUtils;
import net.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import net.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import net.dreamerzero.chatregulator.utils.TypeUtils.SourceType;
import net.kyori.adventure.audience.Audience;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ChatListener {
    private MainConfig.Config config;
    private final ProxyServer server;
    private final ConfigManager cManager;
    private final CommandUtils cUtils;
    private final Replacer rUtils;

    /**
     * ChatListener Constructor
     * @param server the proxy server
     */
    public ChatListener(final ProxyServer server) {
        this.server = server;
        this.config = Configuration.getConfig();
        this.cManager = new ConfigManager();
        this.cUtils = new CommandUtils(server);
        this.rUtils = new Replacer();
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

        if(config.getUnicodeConfig().enabled() && !player.hasPermission("chatregulator.bypass.unicode")){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(message);
            if(uCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, InfractionType.UNICODE, uCheck)){
                event.setResult(ChatResult.denied());
                return;
            }
        }

        if(config.getFloodConfig().enabled() && !player.hasPermission("chatregulator.bypass.flood")){
            FloodCheck fCheck = new FloodCheck();
            fCheck.check(message);
            if(fCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, InfractionType.FLOOD, fCheck)) {
                event.setResult(config.getFloodConfig().getControlType() == ControlType.BLOCK ?
                    ChatResult.denied() :
                    ChatResult.message(fCheck.replaceInfraction()));
                return;
            }
        }

        if(config.getInfractionsConfig().enabled() && !player.hasPermission("chatregulator.bypass.infractions")){
            InfractionCheck iCheck = new InfractionCheck();
            iCheck.check(message);
            if(iCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, InfractionType.REGULAR, iCheck)) {
                event.setResult(config.getInfractionsConfig().getControlType() == ControlType.BLOCK ?
                    ChatResult.denied() :
                    ChatResult.message(iCheck.replaceInfractions()));
                return;
            }
        }

        if(config.getSpamConfig().enabled() && !player.hasPermission("chatregulator.bypass.spam")){
            SpamCheck sCheck = new SpamCheck(infractionPlayer, SourceType.CHAT);
            sCheck.check(message);
            if(sCheck.isInfraction() && (config.getSpamConfig().getCooldownConfig().enabled()
                    && infractionPlayer.getTimeSinceLastMessage() < config.getSpamConfig().getCooldownConfig().limit()
                    || !config.getSpamConfig().getCooldownConfig().enabled())
                && !callChatViolationEvent(infractionPlayer, message, InfractionType.SPAM, sCheck)) {

                    event.setResult(ChatResult.denied());
                    return;
            }
        }

        if(config.getFormatConfig().enabled()){
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
    private boolean callChatViolationEvent(InfractionPlayer player, String message, InfractionType type, AbstractCheck detection) {
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new ChatViolationEvent(player, type, detection, message)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(message);
            } else {
                approved.set(false);
                DebugUtils.debug(player, message, type, detection);
                Statistics.addViolationCount(type);
                cManager.sendWarningMessage(player, type);
                cManager.sendAlertMessage(Audience.audience(
                    server.getAllPlayers().stream()
                        .filter(op -> op.hasPermission("chatregulator.notifications"))
                        .collect(Collectors.toList())),
                    player, type);

                player.addViolation(type);
                cUtils.executeCommand(type, player);
            }
        });
        return approved.get();
    }

}
