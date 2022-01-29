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
import me.dreamerzero.chatregulator.result.IReplaceable;
import me.dreamerzero.chatregulator.result.ReplaceableResult;
import me.dreamerzero.chatregulator.utils.AtomicString;
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
        AtomicString message = new AtomicString(event.getMessage());
        final InfractionPlayer infractor = InfractionPlayer.get(player);

        if(GeneralUtils.allowedPlayer(player, InfractionType.UNICODE)){
            new UnicodeCheck().check(message.get()).thenAcceptAsync(result -> {
                if(GeneralUtils.callViolationEvent(infractor, message.get(), InfractionType.UNICODE, result, SourceType.CHAT)){
                    if(config.getUnicodeConfig().isBlockable()){
                        event.setResult(ChatResult.denied());
                        continuation.resume();
                        return;
                    }
                    if(result instanceof ReplaceableResult){
                        ReplaceableResult replaceableResult = (ReplaceableResult)result;
                        String messageReplaced = replaceableResult.replaceInfraction();
                        event.setResult(ChatResult.message(messageReplaced));
                        message.set(messageReplaced);
                    }
                }
            });
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.CAPS)){
            new CapsCheck().check(message.get()).thenAcceptAsync(result -> {
                if(GeneralUtils.checkAndCall(infractor, message.get(), InfractionType.CAPS, result, SourceType.CHAT)){
                    if(config.getCapsConfig().isBlockable()){
                        event.setResult(ChatResult.denied());
                        continuation.resume();
                        return;
                    }
                    String messageReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(ChatResult.message(messageReplaced));
                    message.set(messageReplaced);
                }
            });

            
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.FLOOD)){
            new FloodCheck().check(message.get()).thenAcceptAsync(result -> {
                if(GeneralUtils.checkAndCall(infractor, message.get(), InfractionType.FLOOD, result, SourceType.CHAT)) {
                    if(config.getFloodConfig().isBlockable()){
                        event.setResult(ChatResult.denied());
                        continuation.resume();
                        return;
                    }
                    String messageReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(ChatResult.message(messageReplaced));
                    message.set(messageReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.REGULAR)){
            new InfractionCheck().check(message.get()).thenAccept(result -> {
                if(GeneralUtils.checkAndCall(infractor, message.get(),InfractionType.REGULAR, result, SourceType.CHAT)) {
                    if(config.getInfractionsConfig().isBlockable()){
                        event.setResult(ChatResult.denied());
                        continuation.resume();
                        return;
                    }
                    String messageReplaced = ((IReplaceable)result).replaceInfraction();
                    event.setResult(ChatResult.message(messageReplaced));
                    message.set(messageReplaced);
                }
            }).join();
        }

        if(GeneralUtils.allowedPlayer(player, InfractionType.SPAM)){
            new SpamCheck(infractor, SourceType.CHAT).check(message.get()).thenAccept(result -> {
                if(GeneralUtils.spamCheck(result, config, infractor) && GeneralUtils.callViolationEvent(infractor, message.get(), InfractionType.SPAM,result, SourceType.CHAT)) {
                    event.setResult(ChatResult.denied());
                    continuation.resume();
                    return;
                }
            });
        }

        if(config.getFormatConfig().enabled()){
            message.set(rUtils.applyFormat(message.get()));
            event.setResult(ChatResult.message(message.get()));
        }

        infractor.lastMessage(message.get());
        continuation.resume();
    }
}
