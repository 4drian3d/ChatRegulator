package io.github._4drian3d.chatregulator.modules.checks;


import io.github._4drian3d.chatregulator.api.checks.UnicodeCheck;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import io.github._4drian3d.chatregulator.utils.TestsUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UnicodeTest {
  @Test
  @DisplayName("Character Check")
  void character() {
    String illegal = "ñn't";
    String expected = "n't";


    assertTrue(UnicodeCheck.builder()
        .charConfig(builder -> builder
            .elements('ñ')
            .controlType(ControlType.BLOCK)
            .detectionMode(DetectionMode.BLACKLIST)
            .build())
        .build().check(TestsUtils.dummyPlayer(), illegal).isDenied());

    CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class,
        UnicodeCheck.builder()
            .charConfig(builder -> builder
                .elements('ñ')
                .controlType(ControlType.REPLACE)
                .detectionMode(DetectionMode.BLACKLIST)
                .build())
            .build()
            .check(TestsUtils.dummyPlayer(), illegal));
    assertTrue(replaceResult.shouldModify());
    assertEquals(expected, replaceResult.replaced());
  }

  @Test
  @DisplayName("Unicode Block Check")
  void blockTest() {
    String illegal = "ƕƘaea";
    String expected = "aea";

    UnicodeCheck check = UnicodeCheck.builder()
        .blocksConfig(builder -> builder
            .elements(Character.UnicodeBlock.LATIN_EXTENDED_B)
            .controlType(ControlType.REPLACE)
            .detectionMode(DetectionMode.BLACKLIST)
            .build()
        )
        .build();
    CheckResult result = check.check(TestsUtils.dummyPlayer(), illegal);

    assertTrue(result.shouldModify());

    CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
    assertEquals(expected, replaceResult.replaced());
  }

  @Test
  @DisplayName("Unicode Script Check")
  void scriptTest() {
    String illegal = "\uD83D\uDE04\u2182#\u21D4\u2CC3\u250E\u23E9\u28BD\u25D7";
    String expected = "\u2182\u2CC3\u28BD";

    UnicodeCheck check = UnicodeCheck.builder()
        .scriptsConfig(builder -> builder
            .elements(Character.UnicodeScript.COMMON)
            .controlType(ControlType.REPLACE)
            .detectionMode(DetectionMode.BLACKLIST)
            .build())
        .build();
    CheckResult result = check.check(TestsUtils.dummyPlayer(), illegal);

    assertTrue(result.shouldModify());

    CheckResult.ReplaceCheckResult replaceResult = assertInstanceOf(CheckResult.ReplaceCheckResult.class, result);
    assertEquals(expected, replaceResult.replaced());
  }

  @ParameterizedTest
  @ValueSource(strings = {"todos los años", "ñandu hahahaha"})
  void builderTest(String msg) {
    assertTrue(UnicodeCheck.builder()
        .charConfig(builder -> builder
            .elements('ñ')
            .detectionMode(DetectionMode.BLACKLIST)
            .controlType(ControlType.BLOCK)
            .build())
        .build()
        .check(TestsUtils.dummyPlayer(), msg).isDenied());
    assertFalse(UnicodeCheck.builder()
        .charConfig(builder -> builder
            .elements("dhanolstuñ ".chars().boxed().map(c -> (Character)(char)(int)c).toArray(Character[]::new))
            .controlType(ControlType.BLOCK)
            .detectionMode(DetectionMode.WHITELIST)
            .build())
        .build()
        .check(TestsUtils.dummyPlayer(), msg).isDenied());
  }
}
