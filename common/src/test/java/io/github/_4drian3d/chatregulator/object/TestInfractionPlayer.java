package io.github._4drian3d.chatregulator.object;

import io.github._4drian3d.chatregulator.api.StringChain;
import io.github._4drian3d.chatregulator.api.enums.SourceType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.common.configuration.Checks;
import io.github._4drian3d.chatregulator.common.configuration.Configuration;
import io.github._4drian3d.chatregulator.common.configuration.ConfigurationContainer;
import io.github._4drian3d.chatregulator.common.configuration.Messages;
import io.github._4drian3d.chatregulator.common.impl.InfractionPlayerBase;
import io.github._4drian3d.chatregulator.common.impl.StringChainImpl;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.Formatter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class TestInfractionPlayer extends InfractionPlayerBase {
  private final StringChainImpl commandChain;
  private final StringChainImpl chatChain;

  private TestInfractionPlayer(ConfigurationContainer<Checks> configurationContainer, TestAudience player) {
    super(UUID.randomUUID(), uuid -> player);
    this.chatChain = new StringChainImpl(configurationContainer);
    this.commandChain = new StringChainImpl(configurationContainer);
  }

  public static TestInfractionPlayer create(ConfigurationContainer<Checks> configurationContainer) {
    return new TestInfractionPlayer(configurationContainer, new TestAudience(""));
  }

  @Override
  protected void sendAlertMessage(ConfigurationContainer<Checks> checksContainer, ConfigurationContainer<Messages> messagesContainer, ConfigurationContainer<Configuration> configurationContainer, Formatter formatter, CheckResult.DetectedResult result, String original) {
  }

  @Override
  protected void executeCommands(ConfigurationContainer<Checks> checksContainer, CheckResult.@NotNull DetectedResult result) {
  }

  @Override
  public @NotNull StringChain getChain(@NotNull SourceType sourceType) {
    return switch (sourceType) {
      case CHAT -> chatChain;
      case COMMAND -> commandChain;
    };
  }
}
