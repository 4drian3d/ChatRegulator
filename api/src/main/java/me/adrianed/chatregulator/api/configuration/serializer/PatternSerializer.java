package me.adrianed.chatregulator.api.configuration.serializer;

import java.lang.reflect.Type;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class PatternSerializer extends ScalarSerializer<Pattern> {
    public static final PatternSerializer INSTANCE = new PatternSerializer();

    protected PatternSerializer() {
        super(Pattern.class);
    }

    @Override
    public Pattern deserialize(Type type, Object obj) throws SerializationException {
        return Pattern.compile(obj.toString(), Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected Object serialize(Pattern item, Predicate<Class<?>> typeSupported) {
        return item.pattern();
    }
    
}
