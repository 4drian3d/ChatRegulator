package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.velocitypowered.api.proxy.Player;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.InfractionPlayer;
import me.dreamerzero.chatregulator.enums.SourceType;
import me.dreamerzero.chatregulator.utils.TestsUtils;

public final class SpamTest {
    @Test
    @DisplayName("Chat Test")
    void chatTest(){
        Player p = TestsUtils.createNormalPlayer("Juan");

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
        Player p = TestsUtils.createNormalPlayer("JuanAlcachofa");

        final InfractionPlayer player = InfractionPlayer.get(p);

        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.COMMAND).join().isInfraction());
    }
}
