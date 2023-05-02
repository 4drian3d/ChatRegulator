package io.github._4drian3d.chatregulator.plugin.source;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import io.github._4drian3d.velocityhexlogger.HexLogger;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Singleton
public final class RegulatorCommandSource implements CommandSource {
    private final PermissionFunction permissionFunction = PermissionFunction.ALWAYS_TRUE;
    @Inject
    private HexLogger logger;

    @Override
    public void sendMessage(@NotNull Component component) {
        this.logger.info(component);
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return permissionFunction.getPermissionValue(permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionFunction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return obj instanceof RegulatorCommandSource;
    }

    @Override
    public String toString() {
        return "RegulatorCommandSource [base=" +
                ", permissionFunction=" +
                permissionFunction +
                "]";
    }
    
}
