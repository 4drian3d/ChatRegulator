package io.github._4drian3d.chatregulator.modules.checks;

import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.impl.StringChainImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public final class CooldownTest {
    @Test
    void testMultipleExecution(@TempDir Path path) {
        var config = ConfigurationContainer.load(
                LoggerFactory.getLogger(CooldownTest.class),
                path,
                Checks.class,
                "checks.conf"
        );
        String string = "hello";
        StringChainImpl stringChain = new StringChainImpl(config);
        assertDoesNotThrow(() -> IntStream.rangeClosed(1, 1_000_000)
                .boxed()
                .parallel()
                .forEach($ -> stringChain.executed(string)));
        assertEquals(5, stringChain.size());

        config.get().getSpamConfig().similarStringCount = 200_000;
        assertDoesNotThrow(() -> IntStream.rangeClosed(1, 1_000_000)
                .boxed()
                .parallel()
                .forEach($ -> stringChain.executed(string)));
        assertEquals(200_000, stringChain.size());
    }
}
