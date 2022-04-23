package me.dreamerzero.chatregulator.modules.checks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.ControlType;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.ReplaceableResult;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Check for invalid characters
 */
public final class UnicodeCheck implements ICheck {
    private final char[] chars;
    private final ControlType control;
    private final Predicate<Character> charPredicate;

    private UnicodeCheck(char[] chars, ControlType control, CharMode mode){
        this.chars = chars;
        this.control = control;
        if(chars == null) {
            this.charPredicate = UnicodeCheck::defaultCharTest;
        } else {
            this.charPredicate = (mode == CharMode.BLACKLIST)
                ? c -> defaultCharTest(c) || charTest(c)
                : c -> defaultCharTest(c) && !charTest(c);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return A {@link Result} if the check was not sucesfull or if the {@link ControlType} is {@link ControlType#BLOCK}
     * else it will return a {@link ReplaceableResult}
     */
    @Override
    public CompletableFuture<Result> check(final @NotNull String string) {
        final char[] charArray = Objects.requireNonNull(string).toCharArray();
        final Set<Character> results = new HashSet<>();

        for(final char character : charArray){
            if(charPredicate.test(character)){
                if(control == ControlType.BLOCK) {
                    return CompletableFuture.completedFuture(new Result(string, true));
                }
                results.add(character);
            }
        }

        return CompletableFuture.completedFuture(results.isEmpty()
            ? new Result(string, false)
            : new ReplaceableResult(results.toString(), true){
                @Override
                public String replaceInfraction(){
                    String replaced = string;
                    for(final char character : results){
                        replaced = replaced.replace(character, ' ');
                    }
                    return replaced;
                }
            });
    }

    public static final boolean defaultCharTest(char c) {
        if(c >= ' ' && c <= '~') {
            return false;
        }
        if(c <= 'ü' && c <= '¿') {
            return false;
        }
        if(c >= '\u00BF' && c <= '\u00FE'){
            return false;
        }
        return true;
    };

    private boolean charTest(char c) {
        for(final char character : this.chars) {
            if(character == c) {
                return true;
            }
        }
        return false;
    };

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }

    public static CompletableFuture<Result> createCheck(String string){
        final var unicode = Configuration.getConfig().getUnicodeConfig();
        return new UnicodeCheck(
                unicode.additionalChars().enabled()
                    ? unicode.additionalChars().chars()
                    : null,
                unicode.getControlType(),
                unicode.additionalChars().charMode()
            ).check(string);
    }

    public static UnicodeCheck.Builder builder(){
        return new UnicodeCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<UnicodeCheck> {
        private char[] chars;
        private ControlType control = ControlType.REPLACE;
        private CharMode mode = CharMode.BLACKLIST;

        private Builder(){}

        /**
         * Set the blocked characters
         * @param chars the characters
         * @return this
         */
        public Builder characters(char... chars){
            this.chars = chars;
            return this;
        }

        /**
         * Set if the check can replace the infraction
         * @param replaceable replaceable or blockable
         * @return this
         */
        public Builder controlType(ControlType control){
            this.control = control;
            return this;
        }

        public Builder charMode(CharMode mode){
            this.mode = mode;
            return this;
        }

        @Override
        public UnicodeCheck build(){
            return new UnicodeCheck(chars, control, mode);
        }

    }

    public static enum CharMode {
        WHITELIST, BLACKLIST
    }
}
