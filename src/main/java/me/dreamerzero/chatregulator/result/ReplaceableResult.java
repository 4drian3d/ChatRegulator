package me.dreamerzero.chatregulator.result;

import org.jetbrains.annotations.Nullable;

/**An result that can be replaced */
public class ReplaceableResult extends Result implements IReplaceable {

    /**
     * Creates a new Replaceable Result
     * @param infractionString the infraction string
     * @param infricted if it was infricted
     */
    public ReplaceableResult(String infractionString, boolean infricted) {
        super(infractionString, infricted);
    }

    @Override
    public @Nullable String replaceInfraction() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
