package me.adrianed.chatregulator.api.logger;

public sealed interface RegulatorLogger permits JavaLogger, SLF4JLogger, Log4JLogger {
    void info(String string);

    void info(String string, Throwable throwable);

    void info(String string, Object... arguments);

    void warn(String string);

    void warn(String string, Throwable throwable);

    void warn(String string, Object... arguments);

    void error(String string);

    void error(String string, Throwable throwable);

    void error(String string, Object... arguments);
}
