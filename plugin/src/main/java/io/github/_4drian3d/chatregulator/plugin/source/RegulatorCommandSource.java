package io.github._4drian3d.chatregulator.plugin.source;

import java.util.Objects;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

/**
 * ChatRegulator CommandSource
 * <p>see https://jd.papermc.io/velocity/3.0.0/com/velocitypowered/api/proxy/ProxyServer.html#getConsoleCommandSource()</p>
 */
public final class RegulatorCommandSource implements CommandSource {
    private PermissionFunction permissionFunction = PermissionFunction.ALWAYS_TRUE;
    private final Audience base;

    public RegulatorCommandSource(EventManager manager, Audience base) {
        this.base = base;

        var permissionsEvent = new PermissionsSetupEvent(this, s -> permissionFunction);
        manager.fire(permissionsEvent)
            .thenAcceptAsync(event -> {
                var function = event.createFunction(this);
                if (function != null) {
                    this.permissionFunction = function;
                } 
            });
    }

    @Override
    public void sendMessage(Component component) {
        this.base.sendMessage(component);
    }

    @Override
    public Tristate getPermissionValue(String permission) {
        return permissionFunction.getPermissionValue(permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(base, permissionFunction);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RegulatorCommandSource other))
            return false;
        return Objects.equals(base, other.base) && Objects.equals(permissionFunction, other.permissionFunction);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RegulatorCommandSource [base=");
        builder.append(base);
        builder.append(", permissionFunction=");
        builder.append(permissionFunction);
        builder.append("]");
        return builder.toString();
    }
    
}
