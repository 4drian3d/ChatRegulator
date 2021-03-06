package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.InfractionPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

final class PlaceholderTest {
    private static final PlainTextComponentSerializer PLAIN_SERIALIZER = PlainTextComponentSerializer.plainText();

    @Test
    @DisplayName("Player Placeholders")
    void playerPlaceholders(@TempDir Path path){
        Player p = TestsUtils.createNormalPlayer("Adrianed_04yt");
        ChatRegulator plugin = TestsUtils.createRegulator(path);

        InfractionPlayer player = InfractionPlayer.get(p);

        MiniMessage mm = MiniMessage.builder()
            .tags(TagResolver.resolver(
                plugin.placeholders().getPlaceholders(player),
                StandardTags.color())
            )
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

        assertEqualsComponent(expectedComponent, componentWithPlaceholders);
    }

    @Test
    @DisplayName("Global Placeholders")
    void globalPlaceholders(@TempDir Path path){
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        MiniMessage mm = MiniMessage.builder()
            .tags(TagResolver.resolver(
                plugin.placeholders().getGlobalPlaceholders(),
                StandardTags.color())
            )
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

        assertEqualsComponent(expectedComponent, componentWithPlaceholders);
    }

    public void assertEqualsComponent(Component first, Component second){
        assertEquals(PLAIN_SERIALIZER.serialize(first), PLAIN_SERIALIZER.serialize(second));
    }
}
