package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.SourceType;

public class SpamTest {
    @Test
    @DisplayName("Chat Test")
    void chatTest(){
        Player p = mock(Player.class);
        when(p.getUsername()).thenReturn("Juan");
        when(p.getUniqueId()).thenReturn(UUID.randomUUID());

        InfractionPlayer player = InfractionPlayer.get(p);

        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.CHAT).join().isInfraction());
    }

    @Test
    @DisplayName("Command Test")
    void commandTest(){
        Player p = mock(Player.class);
        when(p.getUsername()).thenReturn("Juan_Alcachofa");
        when(p.getUniqueId()).thenReturn(UUID.randomUUID());

        final InfractionPlayer player = InfractionPlayer.get(p);

        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.COMMAND).join().isInfraction());
    }
}
