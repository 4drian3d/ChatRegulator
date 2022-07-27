package me.dreamerzero.chatregulator.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

/**
 * Configuration Loader
 */
public final class Loader {
    private Loader(){}

    private static final String CONFIG_HEADER = """
        ChatRegulator | by 4drian3d
        Check the function of each configuration option at
        https://github.com/4drian3d/ChatRegulator/wiki/Configuration""";
    private static final String MESSAGES_HEADER = """
        ChatRegulator | by 4drian3d
        To modify the plugin messages and to use the plugin in general
        I recommend that you have a basic knowledge of MiniMessage
        Guide: https://docs.adventure.kyori.net/minimessage.html#format
        Spanish Guide: https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3""";
    private static final String BLACKLIST_HEADER = """
        ChatRegulator | by 4drian3d
        Blacklist of Commands and Regular Expressions
        To test each regular expression, use:
        https://regex101.com/
        If you are using patterns that include '\\', replace them with '\\\\'""";

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
            if(checkConfig(blacklistConfig)){
                logger.error("Your blacklist configuration contains '\\' character. Please change all of its usage for '\\\\'");
            }
            logger.error("Could not load blacklist.conf file, error: {}", exception.getMessage());
            return null;
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
}
