package io.github._4drian3d.chatregulator.objects;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;
import com.velocitypowered.api.proxy.player.ResourcePackInfo.Builder;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import com.velocitypowered.api.util.ProxyVersion;

import io.github._4drian3d.chatregulator.plugin.utils.TestsUtils;
import net.kyori.adventure.text.Component;

public final class TestProxy implements ProxyServer {
    private PluginManager pManager;
    private EventManager eManager;
    private CommandManager cManager;

    public TestProxy(){
        this.pManager = new TestPluginManager();
        this.eManager = new TestEventManager();
        this.cManager = new TestCommandManager();
    }

    @Override
    public RegisteredServer createRawRegisteredServer(ServerInfo arg0) {
        return null;
    }

    @Override
    public Builder createResourcePackBuilder(String arg0) {
        return null;
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return Set.<Player>of();
    }

    @Override
    public Collection<RegisteredServer> getAllServers() {
        return Set.of();
    }

    @Override
    public InetSocketAddress getBoundAddress() {
        return null;
    }

    @Override
    public ChannelRegistrar getChannelRegistrar() {
        return null;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.cManager;
    }

    @Override
    public ProxyConfig getConfiguration() {
        return null;
    }

    @Override
    public ConsoleCommandSource getConsoleCommandSource() {
        return new ConsoleCommandSource(){
            @Override
            public Tristate getPermissionValue(String arg0) {
                return Tristate.TRUE;
            }
        };
    }

    @Override
    public EventManager getEventManager() {
        return this.eManager;
    }

    @Override
    public Optional<Player> getPlayer(String name) {
        return Optional.of(TestsUtils.createNormalPlayer(name, null).getPlayer());
    }

    @Override
    public Optional<Player> getPlayer(UUID arg0) {
        return Optional.of(TestsUtils.createRandomNormalPlayer());
    }

    @Override
    public int getPlayerCount() {
        return 69;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pManager;
    }

    @Override
    public Scheduler getScheduler() {
        return new Scheduler(){
            @Override
            public TaskBuilder buildTask(Object arg0, Runnable arg1) {
                return null;
            }

            @Override
            public TaskBuilder buildTask(@NotNull Object plugin, @NotNull Consumer<ScheduledTask> consumer) {
                return null;
            }

            @Override
            public @NotNull Collection<ScheduledTask> tasksByPlugin(@NotNull Object plugin) {
                return null;
            }
        };
    }

    @Override
    public Optional<RegisteredServer> getServer(String arg0) {
        return Optional.empty();
    }

    @Override
    public ProxyVersion getVersion() {
        return new ProxyVersion("asd", "Peruviankkit", "3.0.1");
    }

    @Override
    public Collection<Player> matchPlayer(String arg0) {
        return Set.<Player>of();
    }

    @Override
    public Collection<RegisteredServer> matchServer(String arg0) {
        return Set.<RegisteredServer>of();
    }

    @Override
    public RegisteredServer registerServer(ServerInfo arg0) {
        return null;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void shutdown(Component arg0) {
    }

    @Override
    public void unregisterServer(ServerInfo arg0) {
    }
}
