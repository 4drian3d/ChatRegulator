package me.adrianed.chatregulator.api.configuration.serializer;

import java.lang.reflect.Type;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class TextReplacementSerializer extends ScalarSerializer<TextReplacementConfig> {
    public static TextReplacementSerializer INSTANCE = new TextReplacementSerializer(TextReplacementConfig.class);

    protected TextReplacementSerializer(Class<TextReplacementConfig> type) {
        super(type);
    }

    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    @Override
    public TextReplacementConfig deserialize(Type type, Object obj) throws SerializationException {
        return TextReplacementConfig.builder()
            .match(Pattern.compile(obj.toString(), Pattern.CASE_INSENSITIVE))
            .replacement(a -> generateReplacement(SERIALIZER.serialize(a.build()))).build();
    }

    static Component generateReplacement(final String result) {
        final int size = result.length() / 2;
        final StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            builder.append('*');
        }
        return Component.text(builder.toString());
    }

    @Override
    protected Object serialize(TextReplacementConfig item, Predicate<Class<?>> typeSupported) {
        return item.matchPattern().toString();
    }
    
}
