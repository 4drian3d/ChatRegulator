package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.InfractionPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PlaceholderTest {

    @Test
    @DisplayName("Player Placeholders")
    void playerPlaceholders(){
        Player p = TestsUtils.createNormalPlayer("Adrianed_04yt");

        InfractionPlayer player = InfractionPlayer.get(p);

        MiniMessage mm = MiniMessage.builder()
            .placeholderResolver(PlaceholderUtils.getPlaceholders(player))
            .build();

        Component componentWithPlaceholders = mm.deserialize(
            "<aqua>Player <player> or <name> with"
            +" <regular> regular infractions,"
            +" <flood> flood infractions,"
            +" <spam> spam infractions,"
            +" <unicode> unicode infractions,"
            +" and <caps> caps infractions");

        Component expectedComponent = Component.text(
            "Player Adrianed_04yt or Adrianed_04yt with"
            +" 0 regular infractions,"
            +" 0 flood infractions,"
            +" 0 spam infractions,"
            +" 0 unicode infractions,"
            +" and 0 caps infractions", NamedTextColor.AQUA);

        assertEquals(expectedComponent, componentWithPlaceholders);
    }

    @Test
    @DisplayName("Global Placeholders")
    void globalPlaceholders(){
        MiniMessage mm = MiniMessage.builder()
            .placeholderResolver(PlaceholderUtils.getGlobalPlaceholders())
            .build();

        Component componentWithPlaceholders = mm.deserialize(
            "<aqua>Global statistics"
            +" <regular> regular infractions,"
            +" <flood> flood infractions,"
            +" <spam> spam infractions,"
            +" and <unicode> unicode infractions");

        Component expectedComponent = Component.text(
            "Global statistics"
            +" 0 regular infractions,"
            +" 0 flood infractions,"
            +" 0 spam infractions,"
            +" and 0 unicode infractions", NamedTextColor.AQUA);

        assertEquals(expectedComponent, componentWithPlaceholders);
    }
}
