package io.github._4drian3d.chatregulator.modules;

import io.github._4drian3d.chatregulator.api.utils.Replacer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplacerTest {
    @Test
    @DisplayName("First Letter Uppercase")
    void firstLetterUppercase(){
        String original = "peruviankkit";
        String expected = "Peruviankkit";

        String replaced = Replacer.firstLetterUppercase(original);
        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Final Dot")
    void finalDot(){
        String original = "peruviankkit";
        String expected = "peruviankkit.";

        String replaced = Replacer.addFinalDot(original);

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Unicode Normalize")
    void unicodeNormalize() {
        String original = "\u1100\uAC00\u11A8";
        String expected = "\u1100\uAC01";

        String replaced = Replacer.unicodeNormalize(original, Normalizer.Form.NFC);

        assertEquals(expected, replaced);
    }

    @Test
    @DisplayName("Full Format")
    void applyFullFormat(){
        String original = "peruviankkit";
        String expected = "Peruviankkit.";

        String replaced = Replacer.applyFormat(original);

        assertEquals(replaced, expected);
    }
}
