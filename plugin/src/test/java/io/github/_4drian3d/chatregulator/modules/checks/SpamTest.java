package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.SpamCheck;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamTest {
    @Test
    @DisplayName("Chat Test")
    void chatTest(@TempDir Path path){
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        InfractionPlayerImpl player = TestsUtils.createNormalPlayer("Juan", plugin);

        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");
        player.lastMessage("holaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.CHAT).join().isInfraction());
    }

    @Test
    @DisplayName("Command Test")
    void commandTest(@TempDir Path path){
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        InfractionPlayerImpl player = TestsUtils.createNormalPlayer("JuanAlcachofa", plugin);

        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");
        player.lastCommand("tell 4drian3d holaaaaaaaaaaaaaaaaa");

        assertTrue(SpamCheck.createCheck(player, "holaaaaaaaa", SourceType.COMMAND).join().isInfraction());
    }
}
