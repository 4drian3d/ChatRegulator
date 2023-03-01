package io.github._4drian3d.chatregulator.plugin.config;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import io.github._4drian3d.chatregulator.plugin.config.Messages.Alert;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.api.result.Result;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.enums.Permission;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

/**
 * Utilities for using the configuration paths in an orderly manner
 */
public final class ConfigManager {
    @Inject
    private ChatRegulator plugin;
    @Inject
    private ProxyServer proxyServer;
    @Inject
    private IFormatter formatter;

    /**
     * Sends the message of a successful
     * warning reset to the command executor
     *
     * @param sender command executor
     * @param type   type of infraction
     * @param player the infraction player
     *               whose warnings have been reset
     */

}
