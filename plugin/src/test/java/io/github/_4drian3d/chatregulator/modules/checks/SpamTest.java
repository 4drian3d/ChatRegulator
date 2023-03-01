package io.github._4drian3d.chatregulator.plugin.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.velocitypowered.api.proxy.Player;

import io.github._4drian3d.chatregulator.api.checks.SpamCheck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.plugin.utils.TestsUtils;

class SpamTest {
    @Test
    @DisplayName("Chat Test")
    void chatTest(){
        Player p = TestsUtils.createNormalPlayer("Juan");

        InfractionPlayerImpl player = InfractionPlayerImpl.get(p);

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

        final InfractionPlayerImpl player = InfractionPlayerImpl.get(p);

        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.COMMAND).join().isInfraction());
    }
}
