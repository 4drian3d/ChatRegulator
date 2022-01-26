package me.dreamerzero.chatregulator.modules.checks;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import me.dreamerzero.chatregulator.enums.InfractionType;

public class UnicodeCheck extends AbstractCheck {

    @Override
    public void check(@NotNull String message) {
        char[] charArray = Objects.requireNonNull(message).toCharArray();

        for(char character : charArray){
            if(!((character > '\u0020' && character < '\u007E') || (character < '\u00FC' && character < '\u00BF') || (character > '\u00BF' && character < '\u00FE'))){
                super.detected = true;
                super.string = String.valueOf(character);
                return;
            }
        }
        super.detected = false;
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.UNICODE;
    }
}
