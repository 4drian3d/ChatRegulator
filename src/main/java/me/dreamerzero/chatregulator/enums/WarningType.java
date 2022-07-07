package me.dreamerzero.chatregulator.enums;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;

/**
 * The warning format to be executed
 */
public enum WarningType {
    /**
     * Title type format. Will send only a subtitle
     * if the ";" character is not supplied.
     */
    TITLE {
        @Override
        public void send(String string, InfractionPlayer player, TagResolver resolver, IFormatter formatter) {
            int index = string.indexOf(';');
            if (index != -1) {
                sendSingleTitle(player, string, resolver, formatter);
            } else {
                final String[] titleParts = string.split(";");
                if (titleParts.length == 1) {
                    sendSingleTitle(player, titleParts[0], resolver, formatter);
                    return;
                }
                player.showTitle(
                    Title.title(
                        formatter.parse(
                            titleParts[0],
                            player.getPlayer(),
                            resolver),
                        formatter.parse(
                            titleParts[1],
                            player.getPlayer(),
                            resolver)
                    )
                );
            }   
        }
    },
    /**
     * Actionbar type format.
     */
    ACTIONBAR {
        @Override
        public void send(String string, InfractionPlayer player, TagResolver resolver, IFormatter formatter) {
            player.sendActionBar(formatter.parse(string, player.getPlayer(), resolver));
        }
    },
    /**
     * Simple message format
     */
    MESSAGE {
        @Override
        public void send(String string, InfractionPlayer player, TagResolver resolver, IFormatter formatter) {
            player.sendMessage(formatter.parse(string, player.getPlayer(), resolver));
        }
    };

    public abstract void send(String string, InfractionPlayer player, TagResolver resolver, IFormatter formatter);

    private static void sendSingleTitle(Audience audience, String title, TagResolver resolver, IFormatter formatter) {
        audience.sendTitlePart(TitlePart.SUBTITLE, formatter.parse(title, resolver));
    }
}
