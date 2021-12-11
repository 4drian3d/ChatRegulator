package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.InfractionPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;

public class PlaceholderTest {

    //TODO: Use a sinlgeton instead of a static class
    @Test
    void playerPlaceholders(){
        MiniMessage mm = MiniMessage.miniMessage();
        Player p = mock(Player.class);
        when(p.getUsername()).thenReturn("4drian3d");
        when(p.getUniqueId()).thenReturn(UUID.randomUUID());

        InfractionPlayer player = InfractionPlayer.get(p);

        PlaceholderResolver placeholders = PlaceholderUtils.getPlaceholders(player);

        Component componentWithPlaceholders = mm.deserialize(
            "<aqua>Player <player> or <name> with"
            +" <regular> regular infractions,"
            +" <flood> flood infractions,"
            +" <spam> spam infractions,"
            +" <unicode> unicode infractions,"
            +" and <caps> caps infractions",
            placeholders);

        Component expectedComponent = Component.text(
            "Player 4drian3d or 4drian3d with"
            +" 0 regular infractions,"
            +" 0 flood infractions,"
            +" 0 spam infractions,"
            +" 0 unicode infractions,"
            +" and 0 caps infractions", NamedTextColor.AQUA);

        assertEquals(expectedComponent, componentWithPlaceholders);
    }

    @Test
    void globalPlaceholders(){
        MiniMessage mm = MiniMessage.miniMessage();

        PlaceholderResolver placeholders = PlaceholderUtils.getGlobalPlaceholders();

        Component componentWithPlaceholders = mm.deserialize(
            "<aqua>Global statistics"
            +" <regular> regular infractions,"
            +" <flood> flood infractions,"
            +" <spam> spam infractions,"
            +" <unicode> unicode infractions,"
            +" and <caps> caps infractions",
            placeholders);

        Component expectedComponent = Component.text(
            "Global statistics"
            +" 0 regular infractions,"
            +" 0 flood infractions,"
            +" 0 spam infractions,"
            +" 0 unicode infractions,"
            +" and 0 caps infractions", NamedTextColor.AQUA);

        assertEquals(expectedComponent, componentWithPlaceholders);
    }
}
