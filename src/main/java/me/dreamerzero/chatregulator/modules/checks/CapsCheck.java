package me.dreamerzero.chatregulator.modules.checks;

import java.util.Locale;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.dreamerzero.chatregulator.config.Configuration;
import me.dreamerzero.chatregulator.enums.InfractionType;

public class CapsCheck extends AbstractCheck implements ReplaceableCheck{

    @Override
    public void check(@NotNull String message) {
        super.string = Objects.requireNonNull(message);
        char[] chararray = message.toCharArray();
        int capcount = 0;
        for(char c : chararray){
            if(Character.isUpperCase(c)) capcount++;
        }

        if(capcount >= Configuration.getConfig().getCapsConfig().limit()){
            super.detected = true;
        }
    }

    @Override
    public @Nullable String replaceInfraction(){
        return super.string.toLowerCase(Locale.ROOT);
    }

    @Override
    public @Nullable String getInfractionWord(){
        return this.string;
    }

    @Override
    public @NotNull InfractionType type() {
        return InfractionType.CAPS;
    }
}
