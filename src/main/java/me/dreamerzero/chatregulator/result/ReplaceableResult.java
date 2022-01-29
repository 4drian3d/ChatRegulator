package me.dreamerzero.chatregulator.result;

public abstract class ReplaceableResult extends Result implements IReplaceable {

    public ReplaceableResult(String infractionString, boolean infricted) {
        super(infractionString, infricted);
    }
}
