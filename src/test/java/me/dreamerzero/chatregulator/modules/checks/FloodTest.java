package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.dreamerzero.chatregulator.result.IReplaceable;

class FloodTest {
    @Test
    @DisplayName("Flood Check")
    void floodCheck(){
        String original = "aa floOoOOOooOod aa";
        String expected = "aa flod aa";

        var result = FloodCheck.builder().limit(5).build().check(original).join();
        assertTrue(result.isInfraction());
        assertTrue(result instanceof IReplaceable);
        String replaced = ((IReplaceable)result).replaceInfraction();

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("MultiFlood Replacement")
    void multiFlood(){
        String original = "helloooooo everyoneeeeeee";

        var result = FloodCheck.createCheck(original).join();
        assertTrue(result.isInfraction());
        assertTrue(result instanceof IReplaceable);
        String replaced = "hello everyone";
        String actual = ((IReplaceable)result).replaceInfraction();
        assertEquals(replaced, actual, actual);
    }
}
