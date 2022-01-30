package me.dreamerzero.chatregulator.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StringTest {
    @Test
    @DisplayName(value = "AtomicString Basic Test")
    void testString(){
        AtomicString astring = new AtomicString("test");
        assertEquals("test", astring.get());

        assertEquals("testing", astring.concatAndGet("ing"));

        assertEquals("hello", astring.setAndGet("hello"));

        assertEquals("hello", astring.getAndSet("new hello"));

        assertEquals("new hello", astring.get());
    }

    @Test
    @DisplayName(value = "AtomicString Concurrent")
    void concurrentTest(){
        final AtomicString aString = new AtomicString("testing");
        CompletableFuture<String> futureString = CompletableFuture.supplyAsync(() -> {
            aString.set("full replaced string");
            return aString.get();
        }).thenApplyAsync(string -> {
            aString.concatAndGet("nose");
            return aString.get();
        }).thenApplyAsync(a -> {
            return aString.setAndGet("full new String");
        });

        String expected = futureString.join();
        String actual = aString.get();

        assertEquals(expected, actual);

    }
}
