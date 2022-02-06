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

/**
 * Check for invalid characters
 */
public class UnicodeCheck implements ICheck {
    private final boolean custom;
    private char[] charscustom = {};
    private final boolean blockable;

    private UnicodeCheck(){
        this(Configuration.getConfig().getUnicodeConfig().isBlockable());
    }

    private UnicodeCheck(boolean blockable){
        this.blockable = blockable;
        this.custom = false;
    }

    private UnicodeCheck(char[] chars, boolean blockable){
        this.charscustom = chars;
        this.custom = true;
        this.blockable = blockable;
    }
    @Override
    public CompletableFuture<Result> check(@NotNull final String string) {
        char[] charArray = Objects.requireNonNull(string).toCharArray();
        final Set<Character> results = new HashSet<>();

        if(custom && charscustom.length != 0){
            for(char character : charArray){
                for(char blockedchar : charscustom){
                    if(character == blockedchar){
                        if(blockable)
                            return CompletableFuture.completedFuture(new Result(string, true));
                        results.add(character);
                    }
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
        return new UnicodeCheck().check(string);
    }

    public static UnicodeCheck.Builder builder(){
        return new UnicodeCheck.Builder();
    }

    public static class Builder{
        private char[] chars;
        private boolean replaceable;

        private Builder(){}

        /**
         * Set the blocked characters
         * @param chars the characters
         * @return this
         */
        public Builder blockedCharacters(char... chars){
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

        public UnicodeCheck build(){
            return chars == null ? new UnicodeCheck(!replaceable) : new UnicodeCheck(chars, !replaceable);
        }

    }
}
