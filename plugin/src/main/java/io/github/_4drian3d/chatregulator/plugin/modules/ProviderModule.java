package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.plugin.config.Checks;
import io.github._4drian3d.chatregulator.plugin.lazy.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Blacklist;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;

public class ProviderModule extends AbstractModule {
    @Singleton
    @Provides
    private CheckProvider<RegexCheck> regex(
            ConfigurationContainer<Checks> configurationContainer,
            ConfigurationContainer<Blacklist> blacklistContainer
    ) {
        return player -> {
            InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.REGEX) && configuration.isEnabled(InfractionType.REGEX)) {
                return RegexCheck.builder()
                        .blockedPatterns(blacklistContainer.get().getBlockedPatterns())
                        .controlType(configuration.getRegexConfig().getControlType())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<CapsCheck> caps(final ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.CAPS) && configuration.isEnabled(InfractionType.CAPS)) {
                return CapsCheck.builder()
                        .limit(configuration.getCapsConfig().limit())
                        .controlType(configuration.getCapsConfig().getControlType())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<CommandCheck> command(
            ConfigurationContainer<Checks> configurationContainer,
            ConfigurationContainer<Blacklist> blacklistContainer
    ) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.BLOCKED_COMMAND) && configuration.isEnabled(InfractionType.BLOCKED_COMMAND)) {
                return CommandCheck.builder()
                        .blockedCommands(blacklistContainer.get().getBlockedCommands())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<FloodCheck> flood(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.FLOOD) && configuration.isEnabled(InfractionType.FLOOD)) {
                return FloodCheck.builder()
                        .limit(configuration.getFloodConfig().getLimit())
                        .controlType(configuration.getFloodConfig().getControlType())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("command")
    private CheckProvider<SpamCheck> commandSpam(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SPAM) && configuration.isEnabled(InfractionType.SPAM)) {
                return SpamCheck.builder()
                        .source(SourceType.COMMAND)
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("chat")
    private CheckProvider<SpamCheck> chatSpam(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SPAM) && configuration.isEnabled(InfractionType.SPAM)) {
                return SpamCheck.builder()
                        .source(SourceType.CHAT)
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<SyntaxCheck> syntax(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks configuration = configurationContainer.get();
            if (infractionPlayer.isAllowed(InfractionType.SYNTAX) && configuration.isEnabled(InfractionType.SYNTAX)) {
                return SyntaxCheck.builder()
                        .allowedCommands(configuration.getSyntaxConfig().getAllowedCommands())
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("command")
    private CheckProvider<CooldownCheck> commandCooldown(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks.Cooldown config = configurationContainer.get().getCooldownConfig();
            if (infractionPlayer.isAllowed(InfractionType.COOLDOWN) && config.enabled()) {
                return CooldownCheck.builder()
                        .limit(config.limit())
                        .timeUnit(config.unit())
                        .source(SourceType.COMMAND)
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    @Named("chat")
    private CheckProvider<CooldownCheck> chatCooldown(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks.Cooldown config = configurationContainer.get().getCooldownConfig();
            if (infractionPlayer.isAllowed(InfractionType.COOLDOWN) && config.enabled()) {
                return CooldownCheck.builder()
                        .limit(config.limit())
                        .timeUnit(config.unit())
                        .source(SourceType.CHAT)
                        .build();
            }
            return null;
        };
    }

    @Singleton
    @Provides
    private CheckProvider<UnicodeCheck> unicode(ConfigurationContainer<Checks> configurationContainer) {
        return player -> {
            final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
            final Checks.Unicode config = configurationContainer.get().getUnicodeConfig();
            if (infractionPlayer.isAllowed(InfractionType.UNICODE) && config.enabled()) {
                if (config.additionalChars().enabled()) {
                    return UnicodeCheck.builder()
                            .characters(config.additionalChars().chars())
                            .detectionMode(config.additionalChars().detectionMode())
                            .controlType(config.getControlType())
                            .build();
                } else {
                    return UnicodeCheck.builder().build();
                }
            }
            return null;
        };
    }
}
