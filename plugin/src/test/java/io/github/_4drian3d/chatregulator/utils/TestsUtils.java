package io.github._4drian3d.chatregulator.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.chatregulator.objects.TestPlayer;
import io.github._4drian3d.chatregulator.objects.TestProxy;
import io.github._4drian3d.chatregulator.plugin.ChatRegulator;
import io.github._4drian3d.chatregulator.plugin.InfractionPlayerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class TestsUtils {
    public static InfractionPlayerImpl dummyPlayer() {
        return new InfractionPlayerImpl(new TestPlayer("", false), null);
    }
    public static InfractionPlayerImpl createNormalPlayer(String name){
        Player player = new TestPlayer(name, false);

        return new InfractionPlayerImpl(player, null);
    }

    public static InfractionPlayerImpl createOperatorPlayer(String name){
        Player player = new TestPlayer(name, true);

        return new InfractionPlayerImpl(player, null);
    }

    public static InfractionPlayerImpl createRandomNormalPlayer() {
        return createNormalPlayer(createRandomString(10));
    }

    private static final Random rm = new Random();

    public static String createRandomString(int limit){
        Set<Character> chars = new HashSet<>();
        for(int i = 0; i < limit; i++){
            char nextCharacter = (char)rm.nextInt(122);
            if (nextCharacter > 'A' && nextCharacter < 'Z' || nextCharacter > 'a')
                chars.add(nextCharacter);
        }
        StringBuilder builder = new StringBuilder();
        chars.forEach(builder::append);

        return builder.toString();
    }

    private static final Logger logger = LoggerFactory.getLogger(TestsUtils.class);

    public static ChatRegulator createRegulator(Path temporaryPath){
        ProxyServer proxy = new TestProxy();

        Injector injector = Guice.createInjector(
                binder -> {
                    binder.bind(ProxyServer.class).toInstance(proxy);
                    binder.bind(Logger.class).toInstance(logger);
                    binder.bind(Path.class).annotatedWith(DataDirectory.class).toInstance(temporaryPath);
                }
        );
        return injector.getInstance(ChatRegulator.class);
    }
}
