package me.adrianed.chatregulator.paper;

import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;

import me.adrianed.chatregulator.api.RegulatorPlugin;
import me.adrianed.chatregulator.api.configuration.Configuration;
import me.adrianed.chatregulator.api.configuration.Messages;
import me.adrianed.chatregulator.api.logger.RegulatorLogger;
import me.adrianed.chatregulator.api.logger.SLF4JLogger;

public class PaperPlugin extends JavaPlugin implements RegulatorPlugin {
    private Path path;
    private RegulatorLogger logger;
    @Override
    public void onEnable() {
        this.logger = new SLF4JLogger(getSLF4JLogger());
        this.path = getDataFolder().toPath();
    }

    @Override
    public Path path() {
        return this.path;
    }

    @Override
    public RegulatorLogger logger() {
        return this.logger;
    }

    @Override
    public Configuration configuration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Messages messages() {
        // TODO Auto-generated method stub
        return null;
    }
}