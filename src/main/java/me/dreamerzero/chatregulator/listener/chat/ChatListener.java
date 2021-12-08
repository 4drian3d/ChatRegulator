package me.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.ResultedEvent.GenericResult;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;


import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.ConfigManager;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.events.ChatViolationEvent;
import me.dreamerzero.chatregulator.modules.Replacer;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.modules.checks.AbstractCheck;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.utils.CommandUtils;
import me.dreamerzero.chatregulator.utils.DebugUtils;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.utils.TypeUtils.ControlType;
import me.dreamerzero.chatregulator.utils.TypeUtils.InfractionType;
import me.dreamerzero.chatregulator.utils.TypeUtils.SourceType;

import java.util.concurrent.atomic.AtomicBoolean;

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

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(message);
            if(uCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, uCheck)){
                event.setResult(ChatResult.denied());
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(message);

            if(cCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, cCheck)){
                if(config.getCapsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    return;
                } else {
                    String messageReplaced = cCheck.replaceInfraction();
                    event.setResult(ChatResult.message(messageReplaced));
                    message = messageReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            FloodCheck fCheck = new FloodCheck();
            fCheck.check(message);
            if(fCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, fCheck)) {
                if(config.getFloodConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    return;
                } else {
                    String messageReplaced = fCheck.replaceInfraction();
                    event.setResult(ChatResult.message(messageReplaced));
                    message = messageReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            InfractionCheck iCheck = new InfractionCheck();
            iCheck.check(message);
            if(iCheck.isInfraction() && !callChatViolationEvent(infractionPlayer, message, iCheck)) {
                if(config.getInfractionsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    return;
                } else {
                    String messageReplaced = iCheck.replaceInfractions();
                    event.setResult(ChatResult.message(messageReplaced));
                    message = messageReplaced;
                }
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            SpamCheck sCheck = new SpamCheck(infractionPlayer, SourceType.CHAT);
            sCheck.check(message);
            if(GeneralUtils.spamCheck(sCheck, config, infractionPlayer) && !callChatViolationEvent(infractionPlayer, message, sCheck)) {
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
     * @param detection the detection
     * @author Espryth
     * @return message of {@link PlayerChatEvent} is approved
     */
    private boolean callChatViolationEvent(InfractionPlayer player, String message, AbstractCheck detection) {
        InfractionType type = detection.type();
        AtomicBoolean approved = new AtomicBoolean(true);
        server.getEventManager().fire(new ChatViolationEvent(player, type, detection, message)).thenAccept(violationEvent -> {
            if(violationEvent.getResult() == GenericResult.denied()) {
                player.lastMessage(message);
            } else {
                approved.set(false);
                DebugUtils.debug(player, message, type, detection);
                Statistics.addViolationCount(type);
                cManager.sendWarningMessage(player, type, detection);
                cManager.sendAlertMessage(server, player, type);

                player.getViolations().addViolation(type);
                cUtils.executeCommand(type, player);
            }
        });
        return approved.get();
    }
}
