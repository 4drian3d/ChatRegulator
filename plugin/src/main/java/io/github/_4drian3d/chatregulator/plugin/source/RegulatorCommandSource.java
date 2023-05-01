package io.github._4drian3d.chatregulator.plugin.source;

import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Singleton
public final class RegulatorCommandSource implements CommandSource {
    private final PermissionFunction permissionFunction = PermissionFunction.ALWAYS_TRUE;
    @Inject // TODO: HEXLogger
    private Logger logger;

    @Override //HEXLOGGER
    public void sendMessage(@NotNull Component component) {
        this.logger.info(component.toString());
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
