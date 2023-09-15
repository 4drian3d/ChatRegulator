/**
 * ChatRegulator API Module
 */
module io.github._4drian3d.chatregulator.api {
    requires static org.jetbrains.annotations;
    requires static org.checkerframework.checker.qual;
    requires net.kyori.adventure;
    requires net.kyori.examination.api;
    requires com.github.benmanes.caffeine;

    exports io.github._4drian3d.chatregulator.api;
    exports io.github._4drian3d.chatregulator.api.result;
    exports io.github._4drian3d.chatregulator.api.enums;
    exports io.github._4drian3d.chatregulator.api.checks;
    exports io.github._4drian3d.chatregulator.api.annotations;
    exports io.github._4drian3d.chatregulator.api.event;
    exports io.github._4drian3d.chatregulator.api.utils;
}