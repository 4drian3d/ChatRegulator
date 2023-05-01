package io.github._4drian3d.chatregulator.plugin;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.github._4drian3d.chatregulator.api.Statistics;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;

import static io.github._4drian3d.chatregulator.plugin.utils.Placeholders.integer;

public final class StatisticsImpl implements Statistics {
    private final AtomicInteger spamCount = new AtomicInteger(0);
    private final AtomicInteger floodCount = new AtomicInteger(0);
    private final AtomicInteger regularCount = new AtomicInteger(0);
    private final AtomicInteger commandCount = new AtomicInteger(0);
    private final AtomicInteger unicodeViolations = new AtomicInteger(0);
    private final AtomicInteger capsViolations = new AtomicInteger(0);
    private final AtomicInteger syntaxViolations = new AtomicInteger(0);
    private final AtomicInteger globalViolations = new AtomicInteger(0);

    /**
     * Add a violation to the overall violation count.
     * @param type the infraction type
     */
    public void addInfractionCount(@NotNull InfractionType type){
        switch(type){
            case SPAM -> this.spamCount.incrementAndGet();
            case FLOOD -> this.floodCount.incrementAndGet();
            case REGULAR -> this.regularCount.incrementAndGet();
            case BCOMMAND -> this.commandCount.incrementAndGet();
            case UNICODE -> this.unicodeViolations.incrementAndGet();
            case CAPS -> this.capsViolations.incrementAndGet();
            case SYNTAX -> this.syntaxViolations.incrementAndGet();
            case NONE -> {}
        }
        this.globalViolations.incrementAndGet();
    }

    /**
     * Obtain the number of infractions of some type
     * @param type the infraction type
     * @return count of the respective infraction type
     */
    @Override
    public int getInfractionCount(@NotNull InfractionType type){
        return switch(type){
            case SPAM -> this.spamCount.get();
            case FLOOD -> this.floodCount.get();
            case REGULAR -> this.regularCount.get();
            case BCOMMAND -> this.commandCount.get();
            case UNICODE -> this.unicodeViolations.get();
            case CAPS -> this.capsViolations.get();
            case SYNTAX -> this.syntaxViolations.get();
            case NONE -> this.globalViolations.get();
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

        return Objects.equals(this.globalViolations, that.globalViolations);
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
