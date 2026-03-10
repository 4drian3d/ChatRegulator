package io.github._4drian3d.chatregulator.object;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.pointer.PointersSupplier;
import org.jetbrains.annotations.NotNull;

public record TestAudience(String name) implements Audience {
  private static final PointersSupplier<TestAudience> POINTERS_SUPPLIER = PointersSupplier.<TestAudience>builder()
      .resolving(Identity.NAME, TestAudience::name)
      .build();

  @Override
  public @NotNull Pointers pointers() {
    return POINTERS_SUPPLIER.view(this);
  }
}
