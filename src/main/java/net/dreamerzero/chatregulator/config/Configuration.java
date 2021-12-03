package net.dreamerzero.chatregulator.config;

import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import net.dreamerzero.chatregulator.modules.checks.FloodCheck;

/**
 * The configuration paths available in the plugin
 */
public class Configuration {
    private Configuration(){}
    private static MainConfig.Config config;
    private static Messages.Config messages;
    private static Blacklist.Config blacklist;

    public static void loadConfig(@NotNull Path path, @NotNull Logger logger){
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
                .header(
                    "ChatRegulator | by 4drian3d\n "+
                    "Check the function of each configuration option at\n"+
                    "https://github.com/4drian3d/ChatRegulator/wiki/Configuration\n"
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
        Path messagesPath = path.resolve("messages.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header(
                    "ChatRegulator | by 4drian3d\n"+
                    "To modify the plugin messages and to use the plugin in general,\n"+
                    "I recommend that you have a basic knowledge of MiniMessage○\n"+
                    "Guide: https://docs.adventure.kyori.net/minimessage.html#format\n"+
                    "Spanish Guide: https://gist.github.com/4drian3d/9ccce0ca1774285e38becb09b73728f3"
                )
            )
            .path(messagesPath)
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
        Path messagesPath = path.resolve("blacklist.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header(
                    "ChatRegulator | by 4drian3d\n"+
                    "Blacklist of Commands and Regular Expressions\n"+
                    "To test each regular expression, use: \n"+
                    "https://regex101.com/"
                )
            )
            .path(messagesPath)
            .build();

        try {
            final CommentedConfigurationNode node = loader.load();
            blacklist = node.get(Blacklist.Config.class);
            node.set(Blacklist.Config.class, blacklist);
            loader.save(node);
        } catch (ConfigurateException exception){
            logger.error("Could not load blacklist.conf file, error: {}", exception.getMessage());
        }
    }

    public static MainConfig.Config getConfig(){
        return config;
    }

    public static Blacklist.Config getBlacklist(){
        return blacklist;
    }

    public static Messages.Config getMessages(){
        return messages;
    }
}
