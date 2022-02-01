package me.dreamerzero.chatregulator.objects;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * String container for chains usage
 */
public final class AtomicString {
    private String string;

    /**
     * Creates a new AtomicString containing a string
     * @param string the string
     */
    public AtomicString(@NotNull String string){
        this.string = Objects.requireNonNull(string);
    }

    /**
     * Concatenates the specified string to the end of this string
     *
     * @param string the concat string
     * @return the concatenated string of the specified string
     */
    public String concatAndGet(@NotNull String string){
        this.string = this.string.concat(Objects.requireNonNull(string));
        return this.string;
    }

    /**
     * Replace the value of this string
     * @param string
     */
    public void set(@NotNull String string){
        this.string = Objects.requireNonNull(string);
    }

    /**
     * Get the old value and set the new value of the string
     * @param string the new string
     * @return the old value and set the new value of the string
     */
    public String getAndSet(String string){
        final String oldString = this.string;
        this.string = Objects.requireNonNull(string);
        return oldString;
    }

    /**
     * Set the new value of the string and return the new value
     * @param string the new value
     * @return the new value
     */
    public String setAndGet(String string){
        this.string = Objects.requireNonNull(string);
        return this.string;
    }

    /**
     * Obtain the container string
     * @return the string
     */
    public String get(){
        return this.string;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || this.getClass() != o.getClass()) return false;
        AtomicString that = (AtomicString)o;
        return that.string.equals(this.string);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.string);
    }

    @Override
    public String toString(){
        return this.string;
    }
}
