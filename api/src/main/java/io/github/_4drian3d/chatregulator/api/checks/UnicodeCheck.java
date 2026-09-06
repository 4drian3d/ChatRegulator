package io.github._4drian3d.chatregulator.api.checks;

import io.github._4drian3d.chatregulator.api.InfractionPlayer;
import io.github._4drian3d.chatregulator.api.enums.ControlType;
import io.github._4drian3d.chatregulator.api.enums.DetectionMode;
import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import io.github._4drian3d.chatregulator.api.result.CheckResult;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import net.kyori.adventure.builder.AbstractBuilder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntPredicate;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

/**
 * Check for invalid characters
 */
@NullMarked
public final class UnicodeCheck implements Check {
  private final UnicodeCheckConfig<Integer> charConfig;
  private final UnicodeCheckConfig<Character.UnicodeBlock> blockConfig;
  private final UnicodeCheckConfig<Character.UnicodeScript> scriptConfig;

  private UnicodeCheck(
      final UnicodeCheckConfig<Integer> charConfig,
      final UnicodeCheckConfig<Character.UnicodeBlock> blockConfig,
      final UnicodeCheckConfig<Character.UnicodeScript> scriptConfig
  ) {
    this.charConfig = requireNonNull(charConfig, "Character config cannot be null");
    this.blockConfig = requireNonNull(blockConfig, "Block config cannot be null");
    this.scriptConfig = requireNonNull(scriptConfig, "Script config cannot be null");
  }

  public record UnicodeCheckConfig<T>(
      Collection<T> elements,
      ControlType controlType,
      DetectionMode detectionMode,
      IntPredicate charPredicate
  ) {
  }

  @Override
  public CheckResult check(InfractionPlayer player, final String string) {
    final IntArrayList codePointList = IntArrayList.toList(requireNonNull(string).codePoints());
    boolean replaced = false;

    if (!this.charConfig.elements.isEmpty()) {
      if (this.charConfig.controlType == ControlType.BLOCK) {
        if (codePointList.intStream().anyMatch(this.charConfig.charPredicate)) {
          return CheckResult.denied(type());
        }
      } else {
        replaced |= codePointList.removeIf(this.charConfig.charPredicate);
      }
    }

    if (!this.blockConfig.elements.isEmpty()) {
      if (this.blockConfig.controlType == ControlType.BLOCK) {
        if (codePointList.intStream().anyMatch(this.blockConfig.charPredicate)) {
          return CheckResult.denied(type());
        }
      } else {
        replaced |= codePointList.removeIf(this.blockConfig.charPredicate);
      }
    }

    if (!this.scriptConfig.elements.isEmpty()) {
      if (this.scriptConfig.controlType == ControlType.BLOCK) {
        if (codePointList.intStream().anyMatch(this.scriptConfig.charPredicate)) {
          return CheckResult.denied(type());
        }
      } else {
        replaced |= codePointList.removeIf(this.scriptConfig.charPredicate);
      }
    }

    if (replaced) {
      return CheckResult.modified(type(), codePointList.intStream()
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString());
    } else {
      return CheckResult.allowed();
    }
  }

  @Override
  public InfractionType type() {
    return InfractionType.UNICODE;
  }

  /**
   * Creates a new Builder
   *
   * @return a new UnicodeCheck Builder
   */
  public static UnicodeCheck.Builder builder() {
    return new UnicodeCheck.Builder();
  }

  /**
   * Unicode Check Builder
   */
  @NullUnmarked
  public static class Builder implements AbstractBuilder<UnicodeCheck> {
    private UnicodeCheckConfig<@NonNull Integer> charConfig;
    private UnicodeCheckConfig<Character.@NonNull UnicodeBlock> blockConfig;
    private UnicodeCheckConfig<Character.@NonNull UnicodeScript> scriptConfig;

    private Builder() {
    }

    /**
     * Set the blocked characters
     *
     * @param chars the characters
     * @return this
     */
    @Deprecated
    public Builder characters(final char @NonNull ... chars) {
      if (this.charConfig == null) {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .elements(chars)
            .build();
      } else {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .elements(chars)
            .controlType(this.charConfig.controlType())
            .detectionMode(this.charConfig.detectionMode())
            .build();
      }
      return this;
    }

    @Deprecated
    public Builder detectionMode(final @NonNull DetectionMode mode) {
      if (this.charConfig == null) {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .detectionMode(mode)
            .build();
      } else {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .elements(this.charConfig.elements().stream().map(i -> (char) ((int) i)).toArray(Character[]::new))
            .controlType(this.charConfig.controlType())
            .detectionMode(mode)
            .build();
      }
      return this;
    }

    /**
     * Set if the check can replace the infraction
     *
     * @param control the control type
     * @return this
     */
    @Deprecated
    public Builder controlType(final @NonNull ControlType control) {
      if (this.charConfig == null) {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .controlType(control)
            .build();
      } else {
        this.charConfig = new UnicodeCheckConfigBuilder.CharsConfigBuilder()
            .elements(this.charConfig.elements().stream().map(i -> (char) ((int) i)).toArray(Character[]::new))
            .controlType(control)
            .detectionMode(this.charConfig.detectionMode())
            .build();
      }
      return this;
    }

    public Builder charConfig(
        final @NonNull Function<UnicodeCheckConfigBuilder<Character, Integer>, UnicodeCheckConfig<@NonNull Integer>> charConfig
    ) {
      this.charConfig = requireNonNull(charConfig.apply(new UnicodeCheckConfigBuilder.CharsConfigBuilder()), "Character config cannot be null");
      return this;
    }

    public Builder blocksConfig(
        final @NonNull Function<UnicodeCheckConfigBuilder<Character.UnicodeBlock, Character.UnicodeBlock>, UnicodeCheckConfig<Character.@NonNull UnicodeBlock>> blockConfig
    ) {
      this.blockConfig = requireNonNull(blockConfig.apply(new UnicodeCheckConfigBuilder.BlocksConfigBuilder()), "Block config cannot be null");
      return this;
    }

    public Builder scriptsConfig(
        final @NonNull Function<UnicodeCheckConfigBuilder<Character.UnicodeScript, Character.UnicodeScript>, UnicodeCheckConfig<Character.@NonNull UnicodeScript>> scriptConfig
    ) {
      this.scriptConfig = requireNonNull(scriptConfig.apply(new UnicodeCheckConfigBuilder.ScriptsConfigBuilder()), "Script config cannot be null");
      return this;
    }

    public static abstract class UnicodeCheckConfigBuilder<T, R> {
      protected Collection<T> elements;
      protected ControlType controlType = ControlType.REPLACE;
      protected DetectionMode detectionMode = DetectionMode.BLACKLIST;

      private UnicodeCheckConfigBuilder() {
      }

      public static final class CharsConfigBuilder extends UnicodeCheckConfigBuilder<Character, Integer> {
        private CharsConfigBuilder() {
          super();
        }

        @Override
        public UnicodeCheckConfigBuilder<Character, Integer> elements(@NonNull Character @NonNull... elements) {
          this.elements = new CharArraySet(Set.of(requireNonNull(elements, "Elements cannot be null")));
          return this;
        }

        public UnicodeCheckConfigBuilder<Character, Integer> elements(char @NonNull ... elements) {
          this.elements = CharArraySet.of(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfigBuilder<Character, Integer> elements(@NonNull AbstractCollection<Character> elements) {
          this.elements = new CharArraySet(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfig<@NonNull Integer> build() {
          requireNonNull(this.elements, "Elements cannot be null");
          requireNonNull(this.controlType, "Control type cannot be null");
          requireNonNull(this.detectionMode, "Detection mode cannot be null");
          final IntArraySet codePoints = this.elements.stream()
              .mapToInt(c -> c)
              .collect(IntArraySet::new, IntArraySet::add, IntArraySet::addAll);
          final IntPredicate charPredicate = (this.detectionMode == DetectionMode.BLACKLIST)
              ? codePoints::contains
              : ((IntPredicate) codePoints::contains).negate();
          return new UnicodeCheckConfig<>(codePoints, controlType, detectionMode, charPredicate);
        }
      }

      private static final class BlocksConfigBuilder extends UnicodeCheckConfigBuilder<Character.UnicodeBlock, Character.UnicodeBlock> {
        private BlocksConfigBuilder() {
          super();
        }

        @Override
        public UnicodeCheckConfigBuilder<Character.UnicodeBlock, Character.UnicodeBlock> elements(Character.UnicodeBlock @NonNull ... elements) {
          this.elements = Set.of(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfigBuilder<Character.UnicodeBlock, Character.UnicodeBlock> elements(@NonNull AbstractCollection<Character.UnicodeBlock> elements) {
          this.elements = Set.copyOf(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfig<Character.@NonNull UnicodeBlock> build() {
          requireNonNull(this.elements, "Elements cannot be null");
          requireNonNull(this.controlType, "Control type cannot be null");
          requireNonNull(this.detectionMode, "Detection mode cannot be null");
          final IntPredicate blockPredicate = codePoint -> this.elements.contains(Character.UnicodeBlock.of(codePoint));
          final IntPredicate finalPredicate = (this.detectionMode == DetectionMode.BLACKLIST)
              ? blockPredicate
              : blockPredicate.negate();
          return new UnicodeCheckConfig<>(this.elements, controlType, detectionMode, finalPredicate);
        }
      }

      private static final class ScriptsConfigBuilder extends UnicodeCheckConfigBuilder<Character.@NonNull UnicodeScript, Character.UnicodeScript> {
        private ScriptsConfigBuilder() {
          super();
        }

        @Override
        public UnicodeCheckConfigBuilder<Character.UnicodeScript, Character.UnicodeScript> elements(Character.@NonNull UnicodeScript @NonNull ... elements) {
          this.elements = Set.of(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfigBuilder<Character.UnicodeScript, Character.UnicodeScript> elements(@NonNull AbstractCollection<Character.UnicodeScript> elements) {
          this.elements = Set.copyOf(requireNonNull(elements, "Elements cannot be null"));
          return this;
        }

        @Override
        public UnicodeCheckConfig<Character.@NonNull UnicodeScript> build() {
          requireNonNull(this.elements, "Elements cannot be null");
          requireNonNull(this.controlType, "Control type cannot be null");
          requireNonNull(this.detectionMode, "Detection mode cannot be null");
          final IntPredicate scriptPredicate = codePoint -> this.elements.contains(Character.UnicodeScript.of(codePoint));
          final IntPredicate finalPredicate = (this.detectionMode == DetectionMode.BLACKLIST)
              ? scriptPredicate
              : scriptPredicate.negate();
          return new UnicodeCheckConfig<>(this.elements, controlType, detectionMode, finalPredicate);
        }
      }

      public abstract UnicodeCheckConfigBuilder<T, R> elements(final @NonNull T @NonNull... elements);

      public abstract UnicodeCheckConfigBuilder<T, R> elements(final @NonNull AbstractCollection<T> elements);

      public UnicodeCheckConfigBuilder<T, R> controlType(final @NonNull ControlType controlType) {
        this.controlType = requireNonNull(controlType, "Control type cannot be null");
        return this;
      }

      public UnicodeCheckConfigBuilder<T, R> detectionMode(final @NonNull DetectionMode detectionMode) {
        this.detectionMode = requireNonNull(detectionMode, "Detection mode cannot be null");
        return this;
      }

      public abstract UnicodeCheckConfig<@NonNull R> build();
    }

    @Override
    public UnicodeCheck build() {
      return new UnicodeCheck(
          requireNonNullElseGet(charConfig,
              () -> new UnicodeCheckConfigBuilder.CharsConfigBuilder().elements(new char[0]).build()),
          requireNonNullElseGet(blockConfig,
              () -> new UnicodeCheckConfigBuilder.BlocksConfigBuilder().elements(new Character.UnicodeBlock[0]).build()),
          requireNonNullElseGet(scriptConfig,
              () -> new UnicodeCheckConfigBuilder.ScriptsConfigBuilder().elements(new Character.UnicodeScript[0]).build())
      );
    }

  }
}
