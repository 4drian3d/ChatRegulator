package io.github._4drian3d.chatregulator.plugin.impl;

import com.google.common.base.Suppliers;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import io.github._4drian3d.chatregulator.plugin.config.Configuration;
import io.github._4drian3d.chatregulator.plugin.config.ConfigurationContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.nio.file.StandardOpenOption.*;

@Singleton
public final class FileLogger {
    private final Supplier<Path> file;
    private final Logger logger;
    private final ConfigurationContainer<Configuration> configurationContainer;

    @Inject
    public FileLogger(
            ConfigurationContainer<Configuration> configurationContainer,
            @DataDirectory Path path,
            Logger logger
    ) {
        this.logger = logger;
        this.configurationContainer = configurationContainer;
        this.file = Suppliers.memoizeWithExpiration(() -> {
            Configuration.Log.File config = configurationContainer.get().getLog().getFile();
            final Path directory = path.resolve("log");
            createDirectory(directory);
            final String filePath = DateTimeFormatter.ofPattern(config.getFileFormat()).format(LocalDateTime.now());
            return directory.resolve(filePath);
        },10, TimeUnit.MINUTES);
    }

    public void log(String string) {
        if (!configurationContainer.get().getLog().getFile().isEnabled()) {
           return;
        }
        final Path path = file.get();
        try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, CREATE, WRITE, APPEND)) {
            final var fileConfig = configurationContainer.get().getLog().getFile();
            final String message = fileConfig.getLogFormat()
                    .replace("time", DateTimeFormatter.ofPattern(fileConfig.getTimeFormat()).format(LocalDateTime.now()))
                    .replace("message", string);
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            logger.warn("Cannot log to file", e);
        }
    }

    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    public void log(Component component) {
        log(SERIALIZER.serialize(component));
    }

    private void createDirectory(Path directory) {
        if (Files.notExists(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
