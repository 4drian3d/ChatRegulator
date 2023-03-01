package io.github._4drian3d.chatregulator.utils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import io.github._4drian3d.chatregulator.objects.TestPlayer;
import io.github._4drian3d.chatregulator.objects.TestProxy;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
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
    public static InfractionPlayerImpl createNormalPlayer(String name, ChatRegulator plugin){
        Player player = new TestPlayer(name, false);

        return new InfractionPlayerImpl(player, plugin);
    }

    public static InfractionPlayerImpl createOperatorPlayer(String name, ChatRegulator plugin){
        Player player = new TestPlayer(name, true);

        return new InfractionPlayerImpl(player, plugin);
    }

    public static ProxyServer createProxy(){
        return new TestProxy();
    }

    public static InfractionPlayerImpl createRandomNormalPlayer(ChatRegulator plugin){
        return createNormalPlayer(createRandomString(10), plugin);
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
        Injector injector = Guice.createInjector(
                binder -> {
                    binder.bind(ProxyServer.class).toInstance(proxy);
                }
        );
        ChatRegulator plugin = new ChatRegulator() {
            StatisticsImpl statistics = new StatisticsImpl();
            @Override
            public IFormatter getFormatter(){
                return new NormalFormatter();
            }

            @Override
            public StatisticsImpl getStatistics(){
                return statistics;
            }
        };
        plugin.reloadConfig();
        return plugin;
    }
}
