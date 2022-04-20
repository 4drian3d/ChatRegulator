package me.dreamerzero.chatregulator.modules.checks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;
import me.dreamerzero.chatregulator.result.ReplaceableResult;
import me.dreamerzero.chatregulator.result.Result;
import net.kyori.adventure.builder.AbstractBuilder;

/**
 * Check for invalid characters
 */
public class UnicodeCheck implements ICheck {
    private char[] charscustom = {};
    private final boolean blockable;
    private Predicate<Character> charPredicate = c -> {
        for(char ch : UnicodeCheck.this.charscustom) {
            if(ch == c) return true;
        }
        return false;
    };

    private UnicodeCheck(char[] chars, boolean blockable, boolean block){
        this.charscustom = chars;
        this.blockable = blockable;
        this.charPredicate = block
            ? this.charPredicate.or(charTest)
            : this.charPredicate.or(charTest).negate();
    }

    @Override
    public CompletableFuture<Result> check(@NotNull final String string) {
        char[] charArray = Objects.requireNonNull(string).toCharArray();
        final Set<Character> results = new HashSet<>();

        if(charscustom.length != 0){
            for(char character : charArray){
                if(charPredicate.test(character)){
                    if(blockable)
                        return CompletableFuture.completedFuture(new Result(string, true));
                    results.add(character);
                }
            }
        } else {
            for(char character : charArray){
                if(charTest.test(character)){
                    if(blockable){
                        return CompletableFuture.completedFuture(new Result(string, true));
                    }
                    results.add(character);
                }
            }
        }

        return results.isEmpty()
            ? CompletableFuture.completedFuture(new Result(string, false))
            : CompletableFuture.completedFuture(new ReplaceableResult(results.toString(), true){
                @Override
                public String replaceInfraction(){
                    String replaced = string;
                    for(char character : results){
                        replaced = replaced.replace(character, ' ');
                    }
                    return replaced;
                }
            });
    }

    private static final Predicate<Character> charTest = c -> !((c >= ' ' && c <= '~') || (c <= 'ü' && c <= '¿') || (c >= '\u00BF' && c <= '\u00FE'));

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }

    public static CompletableFuture<Result> createCheck(String string){
        return Configuration.getConfig().getUnicodeConfig().additionalChars().enabled()
            ? new UnicodeCheck(
                Configuration.getConfig().getUnicodeConfig().additionalChars().chars(),
                Configuration.getConfig().getUnicodeConfig().isBlockable(),
                true
            ).check(string)
            : new UnicodeCheck(
                new char[0],
                Configuration.getConfig().getUnicodeConfig().isBlockable(),
                true
            ).check(string);
    }

    public static UnicodeCheck.Builder builder(){
        return new UnicodeCheck.Builder();
    }

    public static class Builder implements AbstractBuilder<UnicodeCheck> {
        private char[] chars;
        private boolean replaceable;
        private boolean block;

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
        public Builder replaceable(boolean replaceable){
            this.replaceable = replaceable;
            return this;
        }

        public Builder blockCharacters(){
            this.block = true;
            return this;
        }

        public Builder allowCharacters() {
            this.block = false;
            return this;
        }

        @Override
        public UnicodeCheck build(){
            return new UnicodeCheck(chars == null
                    ? new char[0]
                    : chars,
                !replaceable, block);
        }

    }
}
