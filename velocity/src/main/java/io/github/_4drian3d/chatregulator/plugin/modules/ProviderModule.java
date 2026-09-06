package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github._4drian3d.chatregulator.api.checks.*;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.api.lazy.CheckProvider;
import io.github._4drian3d.chatregulator.plugin.impl.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.common.configuration.Blacklist;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;

public class ProviderModule extends AbstractModule {
  @Singleton
  @Provides
  private CheckProvider<RegexCheck> regex(
      final ConfigurationContainer<Checks> configurationContainer,
      final ConfigurationContainer<Blacklist> blacklistContainer
  ) {
    return player -> {
      InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks configuration = configurationContainer.get();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.REGEX)) {
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
      final Checks.Caps configuration = configurationContainer.get().getCapsConfig();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.CAPS)) {
        return CapsCheck.builder()
            .limit(configuration.limit())
            .controlType(configuration.getControlType())
            .algorithm(configuration.getAlgorithm())
            .build();
      }
      return null;
    };
  }

  @Singleton
  @Provides
  private CheckProvider<CommandCheck> command(
      final ConfigurationContainer<Checks> configurationContainer,
      final ConfigurationContainer<Blacklist> blacklistContainer
  ) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.BLOCKED_COMMAND)) {
        return CommandCheck.builder()
            .blockedCommands(blacklistContainer.get().getBlockedCommands())
            .build();
      }
      return null;
    };
  }

  @Singleton
  @Provides
  private CheckProvider<FloodCheck> flood(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks configuration = configurationContainer.get();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.FLOOD)) {
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
  private CheckProvider<SpamCheck> commandSpam(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks configuration = configurationContainer.get();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.SPAM)) {
        return SpamCheck.builder()
            .source(SourceType.COMMAND)
            .similarLimit(configuration.getSpamConfig().getSimilarStringCount())
            .build();
      }
      return null;
    };
  }

  @Singleton
  @Provides
  @Named("chat")
  private CheckProvider<SpamCheck> chatSpam(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks configuration = configurationContainer.get();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.SPAM)) {
        return SpamCheck.builder()
            .source(SourceType.CHAT)
            .similarLimit(configuration.getSpamConfig().getSimilarStringCount())
            .build();
      }
      return null;
    };
  }

  @Singleton
  @Provides
  private CheckProvider<SyntaxCheck> syntax(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks configuration = configurationContainer.get();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.SYNTAX)) {
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
  private CheckProvider<CooldownCheck> commandCooldown(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks.Cooldown config = configurationContainer.get().getCooldownConfig();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.COOLDOWN)) {
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
  private CheckProvider<CooldownCheck> chatCooldown(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks.Cooldown config = configurationContainer.get().getCooldownConfig();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.COOLDOWN)) {
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
  private CheckProvider<UnicodeCheck> unicode(final ConfigurationContainer<Checks> configurationContainer) {
    return player -> {
      final InfractionPlayerImpl infractionPlayer = (InfractionPlayerImpl) player;
      final Checks.Unicode config = configurationContainer.get().getUnicodeConfig();
      if (infractionPlayer.canBeTestBy(configurationContainer, InfractionType.UNICODE)) {
        final UnicodeCheck.Builder builder = UnicodeCheck.builder();
        if (config.additionalChars().enabled()) {
          builder.charConfig(configBuilder ->
              ((UnicodeCheck.Builder.UnicodeCheckConfigBuilder.CharsConfigBuilder) (configBuilder))
                  .elements(config.additionalChars().chars())
                  .detectionMode(config.additionalChars().detectionMode())
                  .controlType(config.additionalChars().getControlType())
                  .build()
          );
        }
        if (config.additionalBlocks().enabled()) {
          builder.blocksConfig(blockBuilder -> blockBuilder
                  .elements(config.additionalBlocks().blocks())
                  .controlType(config.additionalBlocks().getControlType())
                  .detectionMode(config.additionalBlocks().detectionMode())
                  .build()
          );
        }
        if (config.additionalScripts().enabled()) {
          builder.scriptsConfig(scriptBuilder -> scriptBuilder
                  .elements(config.additionalScripts().scripts())
                  .controlType(config.additionalScripts().getControlType())
                  .detectionMode(config.additionalScripts().detectionMode())
                  .build()
          );
        }
        return builder.build();
      }
      return null;
    };
  }
}
