package me.dreamerzero.chatregulator.modules.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.chatregulator.enums.InfractionType;

/**
 * Check for invalid characters
 */
public class UnicodeCheck extends AbstractCheck implements ReplaceableCheck {
    private final List<Character> results;
    private String original;

    public UnicodeCheck(){
        results = new ArrayList<>();
    }

    @Override
    public void check(@NotNull String message) {
        original = message;
        char[] charArray = Objects.requireNonNull(message).toCharArray();
        super.detected = false;

        for(char character : charArray){
            if(!((character > '\u0020' && character < '\u007E') || (character < '\u00FC' && character < '\u00BF') || (character > '\u00BF' && character < '\u00FE'))){
                super.detected = true;
                results.add(character);
            }
        }

        if(detected){
            super.string = results.toString();
        }

    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }

    @Override
    public @Nullable String replaceInfraction() {
        String replaced = original;
        for(var character : results){
            replaced = replaced.replace(character, ' ');
        }
        return replaced;
    }
}
