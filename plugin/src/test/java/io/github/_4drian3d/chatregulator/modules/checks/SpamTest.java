package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.SpamCheck;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.impl.StringChainImpl;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamTest {
    @Test
    @DisplayName("Spam Test")
    void chatTest(@TempDir Path path) {
        var configContainer = ConfigurationContainer.load(
                LoggerFactory.getLogger(SpamTest.class),
                path,
                Checks.class,
                "checks.hocon"
        );
        InfractionPlayerImpl player = TestsUtils.playerFrom(configContainer);
        StringChainImpl chatChain = player.getChain(SourceType.CHAT);
        final String string = "holaaaaaaaa";

        for (int i = 0; i < 5; i++) {
            chatChain.executed(string);
        }

        SpamCheck.Builder builder = SpamCheck.builder().source(SourceType.CHAT);

        assertTrue(builder.similarLimit(5).build().check(player, string).isDenied());
        assertTrue(builder.similarLimit(6).build().check(player, string).isAllowed());
    }
}
