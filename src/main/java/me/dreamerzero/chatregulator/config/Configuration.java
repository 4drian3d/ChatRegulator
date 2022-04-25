package me.dreamerzero.chatregulator.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import me.dreamerzero.chatregulator.modules.checks.FloodCheck;

/**
 * The configuration paths available in the plugin
 */
public final class Configuration {
    private Configuration(){}
    private static MainConfig.Config config;
    private static Messages.Config messages;
    private static Blacklist.Config blacklist;

    /**
     * Loads the plugin configuration
     * @param path plugin path
     * @param logger plugin logger
     */
    public static void loadConfig(@NotNull Path path, @NotNull Logger logger){
        Objects.requireNonNull(path, "plugin path");
        Objects.requireNonNull(logger, "plugin logger");
        loadMainConfig(path, logger);
        loadMessagesConfig(path, logger);
        loadBlacklistConfig(path, logger);

        FloodCheck.setFloodRegex();
    }

    private static void loadMainConfig(Path path, Logger logger){
        Path configPath = path.resolve("config.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header("""
                    ChatRegulator | by 4drian3d
                    Check the function of each configuration option at
                    https://github.com/4drian3d/ChatRegulator/wiki/Configuration"""
                )
            )
            .path(configPath)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(MainConfig.Config.class);
            node.set(MainConfig.Config.class, config);
            loader.save(node);
        } catch (ConfigurateException exception){
            logger.error("Could not load config.conf file, error: {}", exception.getMessage());
        }
    }

    private static void loadMessagesConfig(Path path, Logger logger){
        Path messagesConfig = path.resolve("messages.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header("""
                    ChatRegulator | by 4drian3d
                    To modify the plugin messages and to use the plugin in general
                    I recommend that you have a basic knowledge of MiniMessage
                    Guide: https://docs.adventure.kyori.net/minimessage.html#format
                    Spanish Guide: https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3"""
                )
            )
            .path(messagesConfig)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            messages = node.get(Messages.Config.class);
            node.set(Messages.Config.class, messages);
            loader.save(node);
        } catch (ConfigurateException exception){
            logger.error("Could not load messages.conf file, error: {}", exception.getMessage());
        }
    }

    private static void loadBlacklistConfig(Path path, Logger logger){
        Path blacklistConfig = path.resolve("blacklist.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .serializers(builder -> {
                    builder.register(Pattern.class, new CustomPatternSerializer());
                })
                .header("""
                    ChatRegulator | by 4drian3d
                    Blacklist of Commands and Regular Expressions
                    To test each regular expression, use:
                    https://regex101.com/
                    If you are using patterns that include '\\', replace them with '\\\\'"""
                )
            )
            .path(blacklistConfig)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            blacklist = node.get(Blacklist.Config.class);
            node.set(Blacklist.Config.class, blacklist);
            loader.save(node);
        } catch (ConfigurateException exception){
            if(checkConfig(blacklistConfig)){
                logger.error("Your blacklist configuration contains '\\' character. Please change all of its usage for '\\\\'");
            }
            logger.error("Could not load blacklist.conf file, error: {}", exception.getMessage());
        }
    }

    private static boolean checkConfig(final Path path) {
        try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while((line = reader.readLine()) != null){
                if(line.indexOf('\\') != -1 && !line.contains("\\\\")) {
                    return true;
                }
            }
            return false;
        } catch(IOException e){
            return false;
        }
    }

    /**
     * Get the main Configuration
     * @return the general configuration
     */
    public static MainConfig.Config getConfig(){
        return config;
    }

    /**
     * Get the Blacklist configuration
     * @return the Blacklist configuration
     */
    public static Blacklist.Config getBlacklist(){
        return blacklist;
    }

    /**
     * Get the Messages Configuration
     * @return the Messages configuration
     */
    public static Messages.Config getMessages(){
        return messages;
    }
}
