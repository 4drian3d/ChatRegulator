package io.github._4drian3d.chatregulator.modules.checks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.plugin.impl.StringChainImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public final class CooldownTest {
  @Test
  void testMultipleExecution(@TempDir Path path) {
    var config = ConfigurationContainer.load(
            LoggerFactory.getLogger(CooldownTest.class),
            path,
            Checks.class,
            "checks.conf"
    );
    StringChainImpl stringChain = new StringChainImpl();
    Injector injector = Guice.createInjector(
        binder -> binder.bind(new TypeLiteral<ConfigurationContainer<Checks>>(){})
                .toInstance(config)
    );
    injector.injectMembers(stringChain);
    String string = "hello";
    assertDoesNotThrow(() -> IntStream.rangeClosed(1, 5_000_000)
            .boxed()
            .parallel()
            .forEach($ -> stringChain.executed(string)));

  }
}
