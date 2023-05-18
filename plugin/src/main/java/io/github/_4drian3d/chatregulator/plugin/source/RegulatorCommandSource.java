package io.github._4drian3d.chatregulator.plugin.source;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.Tristate;
import io.github._4drian3d.velocityhexlogger.HexLogger;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class RegulatorCommandSource implements CommandSource {
    private final PermissionFunction permissionFunction = PermissionFunction.ALWAYS_TRUE;
    @Inject
    private HexLogger logger;

    @Override
    @SuppressWarnings("all")
    public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
        this.logger.info(message);
    }

    @Override
    public Tristate getPermissionValue(String permission) {
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
