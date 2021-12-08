package me.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;


import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.config.MainConfig;
import me.dreamerzero.chatregulator.modules.Replacer;
import me.dreamerzero.chatregulator.modules.checks.CapsCheck;
import me.dreamerzero.chatregulator.modules.checks.FloodCheck;
import me.dreamerzero.chatregulator.modules.checks.InfractionCheck;
import me.dreamerzero.chatregulator.modules.checks.SpamCheck;
import me.dreamerzero.chatregulator.modules.checks.UnicodeCheck;
import me.dreamerzero.chatregulator.utils.GeneralUtils;
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

public class ChatListener {
    private MainConfig.Config config;
    private final Replacer rUtils;

    /**
     * ChatListener Constructor
     */
    public ChatListener() {
        this.config = Configuration.getConfig();
        this.rUtils = new Replacer();
    }

    /**
     * Chat Listener for detections
     * @param event the chat event
     */
    @Subscribe
    public void onChat(PlayerChatEvent event, Continuation continuation) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        InfractionPlayer infractionPlayer = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(message);
            if(uCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, message, uCheck, SourceType.CHAT)){
                event.setResult(ChatResult.denied());
                continuation.resume();
                return;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(message);

            if(cCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, message, cCheck, SourceType.CHAT)){
                if(config.getCapsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
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
            if(fCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, message, fCheck, SourceType.CHAT)) {
                if(config.getFloodConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
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
            if(iCheck.isInfraction() && !GeneralUtils.callViolationEvent(infractionPlayer, message, iCheck, SourceType.CHAT)) {
                if(config.getInfractionsConfig().getControlType() == ControlType.BLOCK){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
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
            if(GeneralUtils.spamCheck(sCheck, config, infractionPlayer) && !GeneralUtils.callViolationEvent(infractionPlayer, message, sCheck, SourceType.CHAT)) {
                event.setResult(ChatResult.denied());
                continuation.resume();
                return;
            }
        }

        if(config.getFormatConfig().enabled()){
            message = rUtils.applyFormat(message);
            event.setResult(ChatResult.message(message));
        }

        infractionPlayer.lastMessage(message);
        continuation.resume();
    }
}
