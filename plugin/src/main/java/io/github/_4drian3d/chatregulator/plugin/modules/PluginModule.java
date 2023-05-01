package io.github._4drian3d.chatregulator.plugin.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.PluginManager;
import io.github._4drian3d.chatregulator.plugin.PlayerManagerImpl;
import io.github._4drian3d.chatregulator.plugin.placeholders.RegulatorExpansion;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.MiniPlaceholderFormatter;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.NormalFormatter;

public class PluginModule extends AbstractModule {
    @Singleton
    @Provides
    private IFormatter formatter(
            PluginManager pluginManager,
            PlayerManagerImpl playerManager
    ) {
        if (pluginManager.isLoaded("miniplaceholders")) {
            RegulatorExpansion.getExpansion(playerManager).register();
            return new MiniPlaceholderFormatter();
        } else {
            return new NormalFormatter();
        }
    }
}
