package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import io.github._4drian3d.chatregulator.plugin.config.*;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class ConfigurationModule extends AbstractModule {
    @Singleton
    @Provides
    private ConfigurationContainer<Configuration> configurationContainer(
            Logger logger,
            @DataDirectory  Path path
    ) {
        return ConfigurationContainer.load(
                logger, path, Configuration.class, "config",
                loader -> loader.shouldCopyDefaults(true)
                        .header(Configuration.HEADER)
        );
    }

    @Singleton
    @Provides
    private ConfigurationContainer<Blacklist> blacklist(
            Logger logger,
            @DataDirectory Path path
    ) {
        return ConfigurationContainer.load(
                logger, path, Blacklist.class, "blacklist",
                loader -> loader.shouldCopyDefaults(true)
                        .header(Blacklist.HEADER)
                        .serializers(builder ->
                                builder.register(Pattern.class, new CustomPatternSerializer())
                        )
        );
    }

    @Singleton
    @Provides
    private ConfigurationContainer<Messages> messages(
            Logger logger,
            @DataDirectory Path path
    ) {
        return ConfigurationContainer.load(
                logger, path, Messages.class, "messages",
                loader -> loader.shouldCopyDefaults(true)
                        .header(Messages.HEADER)
        );
    }
}
