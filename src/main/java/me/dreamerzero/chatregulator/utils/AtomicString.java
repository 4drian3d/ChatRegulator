package me.dreamerzero.chatregulator.utils;

public class AtomicString {
    private String string;

    public AtomicString(String string){
        this.string = string;
    }

    public String concatAndGet(String string){
        this.string = this.string.concat(string);
        return this.string;
    }

    public void set(String string){
        this.string = string;
    }

    public String getAndSet(String string){
        final String oldString = this.string;
        this.string = string;
        return oldString;
    }

    public String get(){
        return this.string;
    }
}
