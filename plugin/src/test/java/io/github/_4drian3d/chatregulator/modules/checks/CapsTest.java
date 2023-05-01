package io.github._4drian3d.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import io.github._4drian3d.chatregulator.api.checks.CapsCheck;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.Loader;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.utils.TestsUtils;

class CapsTest {
    @Test
    @DisplayName("Caps Test")
    void capsTest(){
        String original = "HELLO EVERYONE";
        String expected = "hello everyone";

        CapsCheck.builder().limit(5).build().check(original).thenAccept(result -> {
            assertTrue(result.isInfraction());
            IReplaceable replaceable = assertInstanceOf(IReplaceable.class, result);
            String replaced = replaceable.replaceInfraction();

            assertEquals(expected, replaced);
        }).join();
    }

    @Test
    void realTest(@TempDir Path path){
        String message = "AAAAAAAAAA";
        ChatRegulator plugin = TestsUtils.createRegulator(path);
        InfractionPlayerImpl player = TestsUtils.createRandomNormalPlayer(plugin);
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(CapsTest.class));
        // TODO: fix this
        assertTrue(player.isAllowed(InfractionType.CAPS));
        //var result = CapsCheck.createCheck(message, config).join();
        /*assertTrue(player.callEvent(
                message, InfractionType.CAPS,
                result, SourceType.CHAT));
        Ieplaceable replaceable = assertInstanceOf(IReplceable.class, result);
        String replaced = replaceable.replaceInfraction();
        assertEquals("aaaaaaaaaa", replaced);*/
    }
}
