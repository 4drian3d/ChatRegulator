package io.github._4drian3d.chatregulator.plugin;

import java.util.Objects;

import io.github._4drian3d.chatregulator.api.Statistics;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;

import static io.github._4drian3d.chatregulator.plugin.placeholders.Placeholders.integerPlaceholder;
import static io.github._4drian3d.chatregulator.plugin.utils.Placeholders.integer;

/**
 * Manages the plugin's internal statistics
 */
public final class StatisticsImpl implements Statistics {
    private int spamCount;
    private int floodCount;
    private int regularCount;
    private int commandCount;
    private int unicodeViolations;
    private int capsViolations;
    private int syntaxViolations;
    private int globalViolations;

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addInfractionCount(@NotNull InfractionType type){
        switch(type){
            case SPAM -> ++this.spamCount;
            case FLOOD -> ++this.floodCount;
            case REGULAR -> ++this.regularCount;
            case BCOMMAND -> ++this.commandCount;
            case UNICODE -> ++this.unicodeViolations;
            case CAPS -> ++this.capsViolations;
            case SYNTAX -> ++this.syntaxViolations;
            case NONE -> {}
        }
        ++this.globalViolations;
    }

    /**
     * Obtain the number of infractions of some type
     * @param type the infraction type
     * @return count of the respective infraction type
     */
    @Override
    public int getInfractionCount(@NotNull InfractionType type){
        return switch(type){
            case SPAM -> this.spamCount;
            case FLOOD -> this.floodCount;
            case REGULAR -> this.regularCount;
            case BCOMMAND -> this.commandCount;
            case UNICODE -> this.unicodeViolations;
            case CAPS -> this.capsViolations;
            case SYNTAX -> this.syntaxViolations;
            case NONE -> this.globalViolations;
        };
    }

    public TagResolver getPlaceholders() {
        return TagResolver.resolver(
                integer("flood", getInfractionCount(InfractionType.FLOOD)),
                integer("spam", getInfractionCount(InfractionType.SPAM)),
                integer("regular", getInfractionCount(InfractionType.REGULAR)),
                integer("command", getInfractionCount(InfractionType.BCOMMAND)),
                integer("unicode", getInfractionCount(InfractionType.UNICODE)),
                integer("caps", getInfractionCount(InfractionType.CAPS)),
                integer("syntax", getInfractionCount(InfractionType.SYNTAX))
        );
    }

    @Override
    public boolean equals(final Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof final StatisticsImpl that)) {
            return false;
        }

        return this.globalViolations == that.globalViolations;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.globalViolations);
    }

    @Override
    public String toString(){
        return "StatisticsImpl["
            +"regular="+this.regularCount
            +",flood="+this.floodCount
            +",spam="+this.spamCount
            +",caps="+this.capsViolations
            +",command="+this.commandCount
            +",unicode="+this.unicodeViolations
            +",syntax="+this.syntaxViolations
            +"]";
    }
}
