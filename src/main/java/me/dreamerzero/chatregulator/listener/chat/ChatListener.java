package me.dreamerzero.chatregulator.listener.chat;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent.ChatResult;
import com.velocitypowered.api.proxy.Player;

import org.jetbrains.annotations.ApiStatus.Internal;

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
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.enums.SourceType;

@Internal
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
    @Subscribe(order = PostOrder.FIRST)
    public void onChat(PlayerChatEvent event, Continuation continuation) {
        if(!event.getResult().isAllowed()){
            continuation.resume();
            return;
        }
        final Player player = event.getPlayer();
        String message = event.getMessage();
        final InfractionPlayer infractor = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            UnicodeCheck uCheck = new UnicodeCheck();
            uCheck.check(message);
            if(GeneralUtils.checkAndCall(infractor, message, uCheck, SourceType.CHAT)){
                if(config.getUnicodeConfig().isBlockable()){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                    return;
                }
                String messageReplaced = uCheck.replaceInfraction();
                event.setResult(ChatResult.message(messageReplaced));
                message = messageReplaced;

            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            CapsCheck cCheck = new CapsCheck();
            cCheck.check(message);

            if(GeneralUtils.checkAndCall(infractor, message, cCheck, SourceType.CHAT)){
                if(config.getCapsConfig().isBlockable()){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                    return;
                }
                String messageReplaced = cCheck.replaceInfraction();
                event.setResult(ChatResult.message(messageReplaced));
                message = messageReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            FloodCheck fCheck = new FloodCheck();
            fCheck.check(message);
            if(GeneralUtils.checkAndCall(infractor, message, fCheck, SourceType.CHAT)) {
                if(config.getFloodConfig().isBlockable()){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                    return;
                }
                String messageReplaced = fCheck.replaceInfraction();
                event.setResult(ChatResult.message(messageReplaced));
                message = messageReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            InfractionCheck iCheck = new InfractionCheck();
            iCheck.check(message);
            if(GeneralUtils.checkAndCall(infractor, message, iCheck, SourceType.CHAT)) {
                if(config.getInfractionsConfig().isBlockable()){
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                    return;
                }
                String messageReplaced = iCheck.replaceInfraction();
                event.setResult(ChatResult.message(messageReplaced));
                message = messageReplaced;
            }
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            SpamCheck sCheck = new SpamCheck(infractor, SourceType.CHAT);
            sCheck.check(message);
            if(GeneralUtils.spamCheck(sCheck, config, infractor) && GeneralUtils.callViolationEvent(infractor, message, sCheck, SourceType.CHAT)) {
                event.setResult(ChatResult.denied());
                continuation.resume();
                return;
            }
        }

        if(config.getFormatConfig().enabled()){
            message = rUtils.applyFormat(message);
            event.setResult(ChatResult.message(message));
        }

        infractor.lastMessage(message);
        continuation.resume();
    }
}
