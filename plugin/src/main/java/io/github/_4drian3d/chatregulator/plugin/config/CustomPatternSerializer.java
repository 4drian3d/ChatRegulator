package io.github._4drian3d.chatregulator.plugin.config;

import java.lang.reflect.Type;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

final class CustomPatternSerializer extends ScalarSerializer<Pattern> {
    CustomPatternSerializer() {
        super(Pattern.class);
    }

    @Override
    public Pattern deserialize(final Type type, final Object obj) throws SerializationException {
        try {
            // Added CASE_INSENSITIVE to improve default detections
            return Pattern.compile(obj.toString(), Pattern.CASE_INSENSITIVE);
        } catch (final PatternSyntaxException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public Object serialize(final Pattern item, final Predicate<Class<?>> typeSupported) {
        return item.pattern();
    }
}
