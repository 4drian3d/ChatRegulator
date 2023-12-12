package io.github._4drian3d.chatregulator.modules.checks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import io.github._4drian3d.chatregulator.api.checks.SpamCheck;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.impl.StringChainImpl;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpamTest {
    @Test
    @DisplayName("Spam Test")
    void chatTest() {
        InfractionPlayerImpl player = TestsUtils.dummyPlayer();
        StringChainImpl chatChain = player.getChain(SourceType.CHAT);
        final String string = "holaaaaaaaa";

        for (int i = 0; i < 5; i++) {
            chatChain.executed(string);
        }

        SpamCheck.Builder builder = SpamCheck.builder().source(SourceType.CHAT);

        assertTrue(builder.similarLimit(5).build().check(player, string).isDenied());
        assertTrue(builder.similarLimit(6).build().check(player, string).isAllowed());
    }

    @Test
    @DisplayName("Similar String Count Configuration")
    void configuredLimitTest(@TempDir Path testPath) {
        final Logger logger = LoggerFactory.getLogger(SpamTest.class);
        final ConfigurationContainer<Checks> configurationContainer = ConfigurationContainer.load(
                logger,
                testPath,
                Checks.class,
                "checks"
        );
        final Injector injector = Guice.createInjector(
                binder -> binder.bind(new TypeLiteral<ConfigurationContainer<Checks>>(){})
                        .toInstance(configurationContainer));

        final InfractionPlayerImpl player = TestsUtils.dummyPlayer();
        final StringChainImpl chain = player.getChain(SourceType.CHAT);
        injector.injectMembers(chain);

        final String string = "holaaaaaaaa";

        final Runnable assertTest = () -> assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                chain.executed(string);
            }
        });
        assertTest.run();

        configurationContainer.get().getSpamConfig().similarStringCount = 3;
        assertTest.run();

        configurationContainer.get().getSpamConfig().similarStringCount = 0;
        assertTest.run();
    }
}
