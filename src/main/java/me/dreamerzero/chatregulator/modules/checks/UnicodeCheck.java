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
    private char[] chars = {};
    private final ControlType control;
    private Predicate<Character> charPredicate = c -> {
        for(final char character : this.chars) {
            if(character == c) {
                System.out.println("charPredicate: TRUE");
                return true;
            }
        }
        return false;
    };

    private UnicodeCheck(char[] chars, ControlType control, CharMode mode){
        this.chars = chars;
        this.control = control;
        if(chars.length == 0)
            this.charPredicate = DEFAULT_CHAR_TEST;
        else
            this.charPredicate = mode == CharMode.BLACKLIST
                ? DEFAULT_CHAR_TEST.or(this.charPredicate)
                : DEFAULT_CHAR_TEST.or(this.charPredicate.negate());
    }

    @Override
    public CompletableFuture<Result> check(@NotNull final String string) {
        char[] charArray = Objects.requireNonNull(string).toCharArray();
        final Set<Character> results = new HashSet<>();

        /*final List<Character> results = Objects.requireNonNull(string)
            .chars()
            .boxed()
            .map(i -> (char)i.intValue())
            .filter(charPredicate::test)
            .toList();

        if(!results.isEmpty()) {
            return control == ControlType.BLOCK
                ? CompletableFuture.completedFuture(new Result(string, true))
                : CompletableFuture.completedFuture(new ReplaceableResult(results.toString(), true){
                    @Override
                    public String replaceInfraction(){
                        String replaced = string;
                        for(final char character : results){
                            replaced = replaced.replace(character, ' ');
                        }
                        return replaced;
                    }
                });
        } else {
            return CompletableFuture.completedFuture(new Result(string, false));
        }*/

        for(final char character : charArray){
            if(charPredicate.test(character)){
                if(control == ControlType.BLOCK) {
                    System.out.println("Retorno ControlType.BLOCk");
                    return CompletableFuture.completedFuture(new Result(string, true));
                }
                results.add(character);
            }
        }

        return results.isEmpty()
            ? CompletableFuture.completedFuture(new Result(string, false))
            : CompletableFuture.completedFuture(new ReplaceableResult(results.toString(), true){
                {
                    System.out.println("Retorno Replaceable result cuando results no esta vacio");
                }
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

    public static final Predicate<Character> DEFAULT_CHAR_TEST = c -> {
        if(c >= ' ' && c <= '~') {
            System.out.println("DEFAULT_CHAR_TEST: TRUE");
            return true;
        }
        if(c <= 'ü' && c <= '¿') {
            System.out.println("DEFAULT_CHAR_TEST: TRUE");
            return true;
        }
        if(c >= '\u00BF' && c <= '\u00FE'){
            System.out.println("DEFAULT_CHAR_TEST: TRUE");
            return true;
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
                    : new char[0],
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
            return new UnicodeCheck(chars == null
                    ? new char[0]
                    : chars,
                control, mode);
        }

    }

    public static enum CharMode {
        WHITELIST, BLACKLIST
    }
}
