package me.dreamerzero.chatregulator.objects;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandMeta.Builder;
import com.velocitypowered.api.command.CommandSource;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class TestCommandManager implements CommandManager {

    @Override
    public CompletableFuture<Boolean> executeAsync(CommandSource arg0, String arg1) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> executeImmediatelyAsync(CommandSource arg0, String arg1) {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public Collection<String> getAliases() {
        return Set.<String>of();
    }

    @Override
    public @Nullable CommandMeta getCommandMeta(String arg0) {
        return null;
    }

    @Override
    public boolean hasCommand(String arg0) {
        return true;
    }

    @Override
    public Builder metaBuilder(String arg0) {
        return null;
    }

    @Override
    public Builder metaBuilder(BrigadierCommand arg0) {
        return null;
    }

    @Override
    public void register(BrigadierCommand arg0) {
    }

    @Override
    public void register(CommandMeta arg0, Command arg1) {
    }

    @Override
    public void unregister(String arg0) {
    }

    @Override
    public void unregister(CommandMeta arg0) {
    }
}
