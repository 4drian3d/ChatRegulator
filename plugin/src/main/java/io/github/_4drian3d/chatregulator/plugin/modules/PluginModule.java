package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.PluginManager;
import io.github._4drian3d.chatregulator.api.PlayerManager;
import io.github._4drian3d.chatregulator.api.Statistics;
import io.github._4drian3d.chatregulator.plugin.impl.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.common.impl.StatisticsImpl;
import io.github._4drian3d.chatregulator.common.placeholders.RegulatorExpansion;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.MiniPlaceholderFormatter;
import io.github._4drian3d.chatregulator.common.placeholders.formatter.Formatter;

public class PluginModule extends AbstractModule {
    private final StatisticsImpl statistics;
    private final PlayerManagerImpl playerManager;

    public PluginModule(StatisticsImpl statistics, PlayerManagerImpl playerManager) {
        this.statistics = statistics;
        this.playerManager = playerManager;
    }

    @Singleton
    @Provides
    private Formatter formatter(
            PluginManager pluginManager,
            PlayerManagerImpl playerManager
    ) {
        if (pluginManager.isLoaded("miniplaceholders")) {
            RegulatorExpansion.getExpansion(playerManager).register();
            return new MiniPlaceholderFormatter();
        } else {
            return new Formatter();
        }
    }

    @Override
    protected void configure() {
        bind(StatisticsImpl.class).toInstance(statistics);
        bind(Statistics.class).toInstance(statistics);
        bind(PlayerManagerImpl.class).toInstance(playerManager);
        bind(PlayerManager.class).toInstance(playerManager);
    }
}
