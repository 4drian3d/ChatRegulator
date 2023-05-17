package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.api.checks.SpamCheck;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.StringChainImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamTest {
    @Test
    @DisplayName("Spam Test")
    void chatTest() {
        InfractionPlayerImpl player = TestsUtils.dummyPlayer();
        StringChainImpl chatChain = player.getChain(SourceType.CHAT);
        final String string = "holaaaaaaaa";

        chatChain.executed(string);
        chatChain.executed(string);
        chatChain.executed(string);
        chatChain.executed(string);
        chatChain.executed(string);

        SpamCheck.Builder builder = SpamCheck.builder().source(SourceType.CHAT);

        assertTrue(builder.similarLimit(5).build().check(player, string).isDenied());
        assertTrue(builder.similarLimit(6).build().check(player, string).isAllowed());
    }
}
