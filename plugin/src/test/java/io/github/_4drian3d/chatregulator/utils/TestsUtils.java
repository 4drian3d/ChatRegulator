package io.github._4drian3d.chatregulator.utils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import io.github._4drian3d.chatregulator.plugin.StatisticsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.objects.TestPlayer;
import io.github._4drian3d.chatregulator.plugin.objects.TestPluginManager;
import io.github._4drian3d.chatregulator.plugin.objects.TestProxy;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.IFormatter;
import io.github._4drian3d.chatregulator.plugin.placeholders.formatter.NormalFormatter;

public final class TestsUtils {
    public static Player createNormalPlayer(String name){
        Player player = new TestPlayer(name, false);

        return player;
    }

    public static Player createOperatorPlayer(String name){
        Player player = new TestPlayer(name, true);

        return player;
    }

    public static ProxyServer createProxy(){
        ProxyServer proxy = new TestProxy();

        return proxy;
    }

    public static Player createRandomNormalPlayer(){
        return createNormalPlayer(createRandomString(10));
    }

    public static String createRandomString(int limit){
        Random rm = new Random();
        Set<Character> chars = new HashSet<>();
        for(int i = 0; i < limit; i++){
            char nextCharacter = (char)rm.nextInt(122);
            if((nextCharacter > 'A' && nextCharacter < 'Z') || (nextCharacter > 'a' && nextCharacter < 'z'))
                chars.add(nextCharacter);
        }
        StringBuilder builder = new StringBuilder();
        chars.forEach(builder::append);

        return builder.toString();
    }

    public static ChatRegulator createRegulator(Path temporaryPath){
        ProxyServer proxy = createProxy();
        Logger logger = LoggerFactory.getLogger(TestsUtils.class);
        ChatRegulator plugin = new ChatRegulator(proxy, logger, temporaryPath, new TestPluginManager()){
            StatisticsImpl statistics = new StatisticsImpl();
            Placeholders placeholders = new Placeholders(this);
            @Override
            public IFormatter getFormatter(){
                return new NormalFormatter();
            }

            @Override
            public StatisticsImpl getStatistics(){
                return statistics;
            }

            @Override
            public Placeholders placeholders() {
                return placeholders;
            }
        };
        plugin.reloadConfig();
        return plugin;
    }
}
