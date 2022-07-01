package me.dreamerzero.chatregulator.utils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dreamerzero.chatregulator.ChatRegulator;
import me.dreamerzero.chatregulator.modules.Statistics;
import me.dreamerzero.chatregulator.objects.TestPlayer;
import me.dreamerzero.chatregulator.objects.TestPluginManager;
import me.dreamerzero.chatregulator.objects.TestProxy;
import me.dreamerzero.chatregulator.placeholders.Placeholders;
import me.dreamerzero.chatregulator.placeholders.formatter.IFormatter;
import me.dreamerzero.chatregulator.placeholders.formatter.NormalFormatter;

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

    public static ChatRegulator createRegulator(){
        ProxyServer proxy = createProxy();
        Logger logger = LoggerFactory.getLogger(TestsUtils.class);
        Path path = Path.of("build", "reports", "tests", "test");
        return new ChatRegulator(proxy, logger, path, new TestPluginManager()){
            Statistics statistics = new Statistics();
            Placeholders placeholders = new Placeholders(this);
            @Override
            public IFormatter getFormatter(){
                return new NormalFormatter();
            }

            @Override
            public Statistics getStatistics(){
                return statistics;
            }

            @Override
            public Placeholders placeholders() {
                return placeholders;
            }
        };
    }
}
