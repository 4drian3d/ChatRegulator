package io.github._4drian3d.chatregulator.api.checks;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import net.kyori.adventure.builder.AbstractBuilder;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Check to detect incoherent messages containing floods
 */
public final class FloodCheck implements Check {
    private static final LoadingCache<Integer, Pattern> floodPatternCache = Caffeine.newBuilder()
            .maximumSize(3)
            .initialCapacity(1)
            .build(length -> Pattern.compile(
                    "(\\w)\\1{"+length+",}",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE)
            );
    private final Pattern pattern;
    private final ControlType controlType;

    private FloodCheck(final Pattern pattern, final ControlType controlType) {
        this.pattern = pattern;
        this.controlType = controlType;
    }

    @Override
    public @NotNull CheckResult check(@NotNull InfractionPlayer player, @NotNull String string) {
        final Matcher matcher = pattern.matcher(requireNonNull(string));

        if (matcher.find()) {
            if (controlType == ControlType.BLOCK) {
                return CheckResult.denied(type());
            } else {
                return CheckResult.modified(type(), matcher.replaceAll(match -> String.valueOf(match.group().charAt(0))));
            }
        }
        return CheckResult.allowed();
    }


    @Override
    public @NotNull InfractionType type() {
        return InfractionType.FLOOD;
    }

    /**
     * Creates a new Builder
     *
     * @return a new FloodCheck Builder
     */
    public static FloodCheck.Builder builder(){
        return new FloodCheck.Builder();
    }

    /** Flood Check Builder */
    public static class Builder implements AbstractBuilder<FloodCheck> {
        private Pattern pattern;
        private ControlType controlType;

        Builder() {}

        public Builder limit(@NonNegative int limit){
            this.pattern = floodPatternCache.get(limit);
            return this;
        }

        public Builder controlType(final @NotNull ControlType controlType) {
            this.controlType = controlType;
            return this;
        }

        @Override
        public @NotNull FloodCheck build(){
            requireNonNull(pattern);
            requireNonNull(controlType);
            return new FloodCheck(this.pattern, this.controlType);
        }
    }
}
