package me.dreamerzero.chatregulator.modules.checks;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import me.dreamerzero.chatregulator.result.IReplaceable;

class FloodTest {
    @ParameterizedTest
    @CsvSource({
        "aa floOoOOOooOod aa, aa flod aa",
        "helloooooooooooooo, hello"
    })
    @DisplayName("Flood Check")
    void floodCheck(String original, String expected){
        var result = FloodCheck.builder().limit(5).build().check(original).join();
        assertTrue(result.isInfraction());
        IReplaceable replaceable = assertInstanceOf(IReplaceable.class, result);
        String replaced = replaceable.replaceInfraction();

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("MultiFlood Replacement")
    void multiFlood(){
        String original = "helloooooo everyoneeeeeee";

        var result = FloodCheck.createCheck(original).join();
        assertTrue(result.isInfraction());
        IReplaceable replaceable = assertInstanceOf(IReplaceable.class, result);
        String replaced = "hello everyone";
        String actual = replaceable.replaceInfraction();
        assertEquals(replaced, actual);
    }
}
