package io.github._4drian3d.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import com.velocitypowered.api.proxy.Player;

import io.github._4drian3d.chatregulator.api.checks.CapsCheck;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.Loader;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.IReplaceable;
import io.github._4drian3d.chatregulator.plugin.utils.TestsUtils;

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
        Player player = TestsUtils.createRandomNormalPlayer();
        Configuration config = Loader.loadMainConfig(path, LoggerFactory.getLogger(CapsTest.class));
        // TODO: fix this
        GeneralUtils utils = null;
        assertTrue(utils.allowedPlayer(player, InfractionType.CAPS, config));
        var result = CapsCheck.createCheck(message, config).join();
        assertTrue(utils.callEvent(
                InfractionPlayerImpl.get(player),
                message, InfractionType.CAPS,
                result, SourceType.CHAT));
        IReplaceable replaceable = assertInstanceOf(IReplaceable.class, result);
        String replaced = replaceable.replaceInfraction();
        assertEquals("aaaaaaaaaa", replaced);
    }
}
