package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.plugin.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Blacklist;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;

import java.util.List;

public class ProviderModule extends AbstractModule {
    @Singleton
    @Provides
    private CheckProvider<InfractionCheck> infractions(
            ConfigurationContainer<Configuration> configurationContainer,
            ConfigurationContainer<Blacklist> blacklistContainer
    ) {
        return player -> {
            InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.REGULAR) && configuration.isEnabled(InfractionType.REGULAR)) {
                return InfractionCheck.builder()
                        .blockedPattern(blacklistContainer.get().getBlockedPatterns())
                        .controlType(configuration.getInfractionsConfig().getControlType())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<CapsCheck> caps(final ConfigurationContainer<Configuration> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.CAPS) && configuration.isEnabled(InfractionType.CAPS)) {
                return CapsCheck.builder()
                        .limit(configuration.getCapsConfig().limit())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<CommandCheck> command(
            ConfigurationContainer<Configuration> configurationContainer,
            ConfigurationContainer<Blacklist> blacklistContainer
    ) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.BCOMMAND) && configuration.isEnabled(InfractionType.BCOMMAND)) {
                return CommandCheck.builder()
                        .blockedCommands(blacklistContainer.get().getBlockedCommands())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<FloodCheck> flood(ConfigurationContainer<Configuration> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.FLOOD) && configuration.isEnabled(InfractionType.FLOOD)) {
                return FloodCheck.builder()
                        .limit(configuration.getFloodConfig().getLimit())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("command")
    private CheckProvider<SpamCheck> commandSpam(ConfigurationContainer<Configuration> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SPAM) && configuration.isEnabled(InfractionType.SPAM)) {
                return SpamCheck.builder()
                        .source(SourceType.COMMAND)
                        // TODO: Configurable spam commands check
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("chat")
    private CheckProvider<SpamCheck> chatSpam(ConfigurationContainer<Configuration> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SPAM) && configuration.isEnabled(InfractionType.SPAM)) {
                return SpamCheck.builder()
                        .source(SourceType.CHAT)
                        // TODO: Configurable spam chat check
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<SyntaxCheck> syntax(ConfigurationContainer<Configuration> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Configuration configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SPAM) && configuration.isEnabled(InfractionType.SPAM)) {
                return SyntaxCheck.builder()
                        // TODO: Configurable allowed commands
                        .allowedCommands(List.of())
                        .build();
            }
            return null;
        };
    }
}
