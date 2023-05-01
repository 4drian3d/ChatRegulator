package io.github._4drian3d.chatregulator.plugin.config;

import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Configuration Loader
 */
public final class Loader {
    private Loader(){}





    public static Configuration loadMainConfig(Path path, Logger logger){
        Path configPath = path.resolve("config.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header(CONFIG_HEADER)
            )
            .path(configPath)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            Configuration config = node.get(Configuration.class);
            node.set(Configuration.class, config);
            loader.save(node);
            return config;
        } catch (ConfigurateException exception){
            logger.error("Could not load config.conf file, error: {}", exception.getMessage());
            return null;
        }
    }

    public static Messages loadMessagesConfig(Path path, Logger logger){
        Path messagesConfig = path.resolve("messages.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header(MESSAGES_HEADER)
            )
            .path(messagesConfig)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            Messages messages = node.get(Messages.class);
            node.set(Messages.class, messages);
            loader.save(node);
            return messages;
        } catch (ConfigurateException exception){
            logger.error("Could not load messages.conf file, error: {}", exception.getMessage());
            return null;
        }
    }

    public static Blacklist loadBlacklistConfig(Path path, Logger logger){
        Path blacklistConfig = path.resolve("blacklist.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .serializers(builder -> 
                    builder.register(Pattern.class, new CustomPatternSerializer())
                )
                .header(BLACKLIST_HEADER)
            )
            .path(blacklistConfig)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            Blacklist blacklist = node.get(Blacklist.class);
            node.set(Blacklist.class, blacklist);
            loader.save(node);
            return blacklist;
        } catch (ConfigurateException exception){
            if (checkConfig(blacklistConfig)){
                logger.error("Your blacklist configuration contains '\\' character. Please change all of its usage for '\\\\'");
            }
            logger.error("Could not load blacklist.conf file, error: {}", exception.getMessage());
            return null;
        }
    }


}
