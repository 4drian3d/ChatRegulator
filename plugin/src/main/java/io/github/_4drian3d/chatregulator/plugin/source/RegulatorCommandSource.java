package io.github._4drian3d.chatregulator.plugin.source;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class RegulatorCommandSource implements CommandSource {
    private final PermissionFunction permissionFunction = PermissionFunction.ALWAYS_TRUE;
    @Inject
    private ComponentLogger logger;

    @Override
    @SuppressWarnings("all")
    public void sendMessage(
            final @NotNull Identity source,
            final @NotNull Component message,
            final @NotNull MessageType type
    ) {
        this.logger.info(message);
    }

    @Override
    public Tristate getPermissionValue(final String permission) {
        return permissionFunction.getPermissionValue(permission);
    }

    @Override
    public String toString() {
        return "RegulatorCommandSource [base=" +
                ", permissionFunction=" +
                permissionFunction +
                "]";
    }
}
